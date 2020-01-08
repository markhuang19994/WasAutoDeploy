NAME Cluster 1 deploy

SSH_URL root@192.168.36.91

SCP ./data/D:/mnt/share_data:wasadmin::755

RUN echo '123' &&\
    echo 456


