# BrisaSensors · Monitoramento do Ar

Serviço de monitoramento do ar do [BrisaSensors](https://github.com/sidartaoss/brisasensors), sistema fictício de monitoramento da qualidade do ar em ambientes internos. Consome o tópico de leituras, grava o histórico em um banco de séries temporais, expõe o valor atual por ambiente e emite alertas quando o CO₂ permanece acima do limite do ambiente por um tempo mínimo. Mantém uma cópia mínima de cada dispositivo (identificador, ambiente e situação), atualizada pelos eventos da gestão de dispositivos.

As fronteiras e decisões do serviço estão no [estudo de caso](https://github.com/sidartaoss/fronteiras-de-microsservicos#7-estudo-de-caso-brisasensors).
