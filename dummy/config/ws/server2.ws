NAME    Cluster server2 deploy

SSH_URL root@localhost:60022

RUN     cd ${ws}/dummy
        tar -C D -zcf d.tar.gz .
        PROJECT_DIR=D://ws_tmp/my_project
        mkdir -p "$$PROJECT_DIR"
        mv d.tar.gz "$$PROJECT_DIR"/d.tar.gz
        mv my_project.war "$$PROJECT_DIR"/my_project.war

SSH_RUN DIR=/mnt/macaque/ws_temp
        mkdir -p "$$DIR"
        if ! mountpoint -q "$$DIR"
        then
            mount -t cifs -o user=iisi,password=P@ssw0rd201603,uid=1000,gid=1000 \
            //finsrv01.iead.local/ws_temp "$$DIR"
        fi
        cd "$$DIR"/my_project
        tar -C . -zxvf d.tar.gz > /dev/null
        rm -rf d.tar.gz


