NAME Cluster server2 deploy

SSH_URL root@localhost:60022

SCP /home/markhuag/Documents/project/source/Learn/WasAutoDeploy/dummy/D:~/mnt/share_data2:::
SCP /home/markhuag/Documents/project/source/Learn/WasAutoDeploy/dummy/extfunc05.war:~/mnt/share_data2/other/was/war/extfunc05.war:ub::755

RUN echo 'Hello ' && \
    echo "I'm server2"


