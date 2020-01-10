NAME     Cluster server1 deploy

SSH_URL  root@localhost:60022

RUN      cd ${ws}/dummy && tar -C D -zcf d.tar.gz .

SCP      ${ws}/dummy/d.tar.gz => /mnt/share_data_test1/pcl/d.tar.gz

SSH_RUN  cd /mnt/share_data_test1/pcl
         tar -C . -zxvf d.tar.gz > /dev/null
         rm -rf d.tar.gz

RUN      rm -rf ${ws}/dummy/d.tar.gz

