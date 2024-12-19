# 使用官方的 code-server 镜像作为基础镜像
FROM shinearcom/code-server:latest

# 设置环境变量
ENV DOCKER_USER=${USER}

# 将宿主机的用户目录和配置文件挂载到容器中
VOLUME ["$HOME/.local", "$HOME/.config", "$PWD"]

# 设置容器启动时的工作目录
WORKDIR /home/coder/project

# 运行 code-server 时绑定端口
EXPOSE 8080

# 设置默认的启动命令
CMD ["code-server", "--bind-addr", "0.0.0.0:8080", "/home/coder/project"]