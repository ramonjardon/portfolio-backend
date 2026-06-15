# Runtime — el binario llega ya compilado desde GitHub Actions
FROM public.ecr.aws/chainguard/wolfi-base:latest

RUN apk add --no-cache zlib curl

WORKDIR /app

COPY --chown=nonroot:nonroot --chmod=500 \
     target/portfolio-backend /app/portfolio-backend

USER nonroot

EXPOSE 8080
ENTRYPOINT ["/app/portfolio-backend"]