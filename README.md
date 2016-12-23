# Network-Performance-Tools

A (soon to be expanding) collection of useful network performance tools, written in Java.

## Tools

#### Iperfer

A tool for measuring network bandwidth.  **Iperfer** sends and receives TCP packets between a
pair of hosts using sockets.
    
When operating in *client mode*, **Iperfer** will send TCP packets to a specific host for a
specified time window and track how much data was sent during that time frame; it will calculate
and display the bandwidth based on how much data was sent in the elapsed time.

When operating in *server mode*, **Iperfer** will receive TCP packets and track how much data was
received during the lifetime of a connection; it will calculate and display the bandwidth based on
how much data was received and how much time elapsed between the reception of the first byte of data
and the last byte of data.

## Usage - Iperfer

#### Client Mode

To operate **Iperfer** in client mode, invoke it as follows:
```
$ java Iperfer -c -h [server hostname] -p [server port] -t [time]
```

* `-c` indicates this is the iperf client that generates the data.
* `server hostname` is the hostname or IP address of the iperf server which will consume data.
* `server port` is the port on which the remote host is waiting to consume data; the port should
  be in the range 1024 < `server port` < 65536.
* `time` is the duration in seconds for which data should be generated.

The presence of the `-c` option will be taken as an indicator that **Iperfer** is being run in
client mode.

After `time` seconds have passed, **Iperfer** will stop sending data, close the connection, and
print a one line summary that includes:
* The total number of bytes sent (in kilobytes.)
* The rate at which traffic could be sent (Mbps.)

#### Server Mode

To operate **Iperfer** in server mode, invoke it as follows:
```
$ java Iperfer -s -p [listen port]
```

* `-s` indicates this is the iperf server which should consume data.
* `listen port` is the port on which the host is waiting to consume data; the port should be in the
  range 1024 < `listen port` < 65536.

The presence of the `-s` option will be taken as an indicator that **Iperfer** is being run in
server mode.

After the client has closed the connection, **Iperfer** will print a one line summary that includes:
* The total number of bytes received (in kilobytes.)
* The rate at which traffic could be read (Mbps.)
