FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER focus

RUN mkdir -p /cos_migrate_tool_v5/log \
    /cos_migrate_tool_v5/tmp \
    /cos_migrate_tool_v5/result \
    /cos_migrate_tool_v5/db

WORKDIR /cos_migrate_tool_v5

COPY . .

VOLUME ["/cos_migrate_tool_v5/"]

ENTRYPOINT ["./start_migrate.sh"]
