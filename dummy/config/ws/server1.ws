NAME Cluster server1 deploy

SSH_URL root@localhost:60022

SCP ${ws}/dummy/D => ~/mnt/share_datac
SCP ${ws}/dummy/extfunc05.war => /mnt/share_datac/other/was/war/extfunc05.war:us::755

RUN echo '123' && \
    echo 456

