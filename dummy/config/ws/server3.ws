NAME        Cluster server1 deploy

SSH_URL     root@172.17.0.4

#           新增一個使用者
DEF         ADD_USER=test001
DEF         USER_PWD=p@ssw0rd

SSH_RUN     userdel test001
            adduser ${ADD_USER}
            echo ${USER_PWD} | passwd ${ADD_USER} --stdin
            cat /etc/passwd

RUN         mkdir -p /tmp/ws_test
            cd /tmp/ws_test || exit
            for i in {1..20}
            do
                touch "dummy_file_$${i}"
            done
            ls -la

SCP         /tmp/ws_test/* => /tmp/ws_test!${ADD_USER}::755

SSH_RUN     ls -la /tmp/ws_test

RUN         rm -rf /tmp/ws_test

SSH_RUN     yum -y update
            yum install -y java-1.8.0-openjdk


