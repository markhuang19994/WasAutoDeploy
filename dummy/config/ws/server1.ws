NAME Cluster 1 deploy

SSH_URL root@localhost:60022

SCP /home/markhuag/Documents/project/source/Learn/WasAutoDeploy/dummy/D:/mnt/share_data:us::755
SCP /home/markhuag/Documents/project/source/Learn/WasAutoDeploy/dummy/extfunc05.war:/mnt/sharedata/other/was/war/extfunc05.war:us::755

RUN echo '123' && \
    echo 456


