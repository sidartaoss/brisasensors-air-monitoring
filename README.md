# BrisaSensors · Monitoramento do Ar

Serviço de monitoramento do ar do [BrisaSensors](https://github.com/sidartaoss/brisasensors), sistema fictício de monitoramento da qualidade do ar em ambientes internos. Consome o tópico de leituras, grava o histórico em um banco de séries temporais, expõe o valor atual por ambiente e emite alertas quando o CO₂ permanece acima do limite do ambiente por um tempo mínimo. Mantém uma cópia mínima de cada dispositivo (identificador, ambiente e situação), atualizada pelos eventos da gestão de dispositivos.

As fronteiras e decisões do serviço estão no [estudo de caso](https://github.com/sidartaoss/fronteiras-de-microsservicos#7-estudo-de-caso-brisasensors).

## Estado da implementação

- Consome a fila `air-monitoring.process-reading.v1.q` com um único consumidor ativo, o que preserva a ordem das leituras; histórico, valor atual e avaliação do alerta acontecem na mesma transação. Mensagens que esgotam as tentativas vão para `air-monitoring.process-reading.v1.dlq`. Porta 8082.
- O alerta dispara quando o CO₂ permanece acima do limite configurado (`PUT /api/devices/{deviceId}/alert`) pelo tempo mínimo; leituras atrasadas entram no histórico sem alterar o valor atual.
- H2 no lugar do banco de séries temporais, e limites e valor atual por dispositivo, até que o ambiente chegue com a cópia local.
- Enquanto a cópia local não existe, a gestão de dispositivos ativa e desativa o monitoramento por HTTP.

As decisões de código estão no guia [Implementação de Microsserviços](https://github.com/sidartaoss/implementacao-de-microsservicos#7-implementação-de-referência-brisasensors).
