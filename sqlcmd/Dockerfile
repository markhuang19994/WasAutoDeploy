From ubuntu:bionic

RUN apt-get update && apt-get install -y gnupg2 && apt-get install -y curl

RUN curl https://packages.microsoft.com/keys/microsoft.asc | apt-key add - && \
    curl https://packages.microsoft.com/config/ubuntu/18.04/prod.list | tee /etc/apt/sources.list.d/msprod.list && \
    apt-get update && \
    ACCEPT_EULA=y DEBIAN_FRONTEND=noninteractive \
    apt-get install -y --no-install-recommends mssql-tools unixodbc-dev && \
    echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bashrc && \
    echo 'export PATH="$PATH:/opt/mssql-tools/bin"' >> ~/.bash_profile

COPY ./sqlcmd_run_file.sh /usr/script/sqlcmd_run_file.sh
RUN chmod +x /usr/script/sqlcmd_run_file.sh && \
    sed -i -E 's/(MinProtocol.*?)TLSv.*?(\S)/\1TLSv1\2/g' /etc/ssl/openssl.cnf

CMD ["sh", "-c", "sleep 999999999999999"]

    
