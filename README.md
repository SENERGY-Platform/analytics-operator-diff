# operator-value-diff

Subtracts the current Value from the previous one.

VAR | Default | Desc
------------- | ------------- | -------------
CONFIG_APPLICATION_ID  | operator-value-diff | Application ID
CONFIG_BOOTSTRAP_SERVERS  | Queried from zookeeper | List of kafka brokers, optional if ZK_Quorum is given
ZK_QUORUM  | localhost:2181 | zookeeper instances
KAFKA_TOPIC  | input-stream | inputstream name
KAFKA_OUTPUT  | output-stream | outputstream name
INPUT_VALUE  | value | The input value to be diffed
OUTPUT_DIFF  | diff | The difference between two values
