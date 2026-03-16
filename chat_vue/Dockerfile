# 使用 Nginx 镜像作为生产环境的基础镜像
FROM nginx:alpine

# 复制前端 dist
COPY ./dist /usr/share/nginx/html

# 👇👇👇 必须加这一行！复制你的 nginx 配置！
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# 暴露 80 端口
EXPOSE 80