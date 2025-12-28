# OAuth 2.1 PKCE 流程图

```mermaid
sequenceDiagram
    autonumber
    
    %% 定义参与者
    participant Client as 客户端 (你的 Python 代码)
    actor User as 用户 / 浏览器
    participant Server as 认证服务器 (Auth Server)

    %% --- 阶段 1: 筹备 (生成密钥对) ---
    Note over Client: 1. 运行 generate_pkce_pair()<br/>生成 code_verifier (私钥/存根)<br/>生成 code_challenge (公钥/标签)

    %% --- 阶段 2: 承诺 (发送 Challenge) ---
    Client->>User: 2. 生成授权链接
    Note right of Client: 链接参数包含:<br/>client_id<br/>response_type=code<br/>code_challenge=【哈希值】

    User->>Server: 3. 浏览器跳转访问 /authorize
    
    Note over Server: 4. 服务器收到请求<br/>暂时存储 code_challenge<br/>等待用户点击同意...

    Server-->>User: 5. 用户同意授权
    Server-->>Client: 6. 重定向回调 (Redirect)<br/>返回 code=SplxlOB... (授权码)

    %% --- 阶段 3: 验证 (出示 Verifier) ---
    Note over Client: 7. 此时客户端手里有:<br/>1. 刚才收到的 code<br/>2. 一开始生成的 code_verifier

    Client->>Server: 8. 后端请求换取 Token (POST /token)
    Note right of Client: payload 数据:<br/>grant_type=authorization_code<br/>code=SplxlOB...<br/>code_verifier=【原始随机字符串】

    %% --- 阶段 4: 服务器核对 ---
    Note over Server: 9. 核心校验逻辑:<br/>找到该 code 对应的 code_challenge<br/>计算 HASH(收到的 code_verifier)<br/>对比: 计算结果 == 存储的 challenge ?

    alt 校验成功 (Verifier 匹配)
        Server-->>Client: 200 OK (返回 Access Token)
        Note left of Server: 证明请求Token的人<br/>就是最初发起授权的人
    else 校验失败 (Verifier 错误)
        Server-->>Client: 400 Bad Request (invalid_grant)
        Note left of Server: 可能是中间人攻击<br/>拒绝颁发 Token
    end
