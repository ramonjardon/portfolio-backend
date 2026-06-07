# ETAPA 1: Construcción
# glibc obligatorio para que el binario sea compatible con el runtime Chainguard
FROM ghcr.io/bell-sw/liberica-native-image-kit-container:jdk-25-nik-25-glibc AS builder

USER root

# Alpaquita usa apk con sus propios paquetes (no Alpine, no Debian)
# build-base = equivalente a build-essential
# zlib-dev   = equivalente a zlib1g-dev
RUN apk add --no-cache build-base zlib-dev

WORKDIR /build
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B
COPY src ./src
RUN ./mvnw -Pnative native:compile -DskipTests -B \
    -Dspring-boot.native-image.builder-arguments="--gc=G1"

# ─── ETAPA 2: Runtime ──────────────────────────────────────────────────────
FROM public.ecr.aws/chainguard/wolfi-base:latest

# Añadimos curl junto a zlib en una sola capa limpia
RUN apk update && apk add --no-cache zlib curl

WORKDIR /app

# Copiamos el binario nativo asegurando que el usuario nonroot sea el dueño y pueda ejecutarlo
COPY --from=builder --chown=nonroot:nonroot --chmod=500 \
     /build/target/portfolio-backend /app/portfolio-backend

# Cambiamos al usuario seguro de Chainguard/Wolfi
USER nonroot

EXPOSE 8080
ENTRYPOINT ["/app/portfolio-backend"]