# 使用 Nginx 镜像作为生产环境的基础镜像
FROM nginx:alpine

# 复制构建好的 dist 文件夹到 Nginx 默认的 HTML 目录
COPY ./dist /usr/share/nginx/html

# 暴露 80 端口
EXPOSE 80