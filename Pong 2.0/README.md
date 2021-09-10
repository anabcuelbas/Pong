# Pong 2.0

## Descrição
Adiciona novos recursos ao programa [Pong 1.0](https://github.com/anabcuelbas/COO_ep_Pong)

Foi implementada uma nova classe chamada *FxBall*, que implementa a interface *IBall* e acrescenta um efeito de rastro na bola.
Além disso, agora o jogo pode possuir mais de uma bola ao mesmo tempo, através de um obstáculo de duplicação e também ter a velocidade
da bola aumentada, através de um obstáculo de boost. Para o gerenciamento de todas as bolas, foi criada a classe *BallManager*.

## Compilando e executando o Pong 2.0
Para compilar o jogo basta executar, no terminal, o comando:
```bash
$ javac *.java
```
Para rodar, existem alguns parâmetros opcionais que podem ser
especificados. Tais parâmetros são recebidos pela linha de comando e, apesar de serem
opcionais, devem ser sempre passados na mesma ordem, quando usados (por exemplo, se
você quiser especificar o segundo parâmetro, deve obrigatoriamente especificar o primeiro
também). A linha de comando para rodar o jogo deve seguir o seguinte padrão:
```bash
$ java Pong <classe_bola> <intervalo_de_tempo> <safe_mode>
```
Para utilizar a bola com rastro, o seguinte comando deve ser executado:
```bash
$ java Pong FxBall
```
Para outro tipo de bola:
```bash
$ java Pong DiamondBall
```

Em relação ao parâmetro `intervalo_de_tempo` , pode-se usá-lo para especificar o
intervalo de tempo mínimo (em milissegundos) que deve ser aguardado entre o
processamento de dois frames consecutivos do jogo. Este parâmetro possui valor padrão
igual a 3.

Por fim, o parâmetro `safe_mode` , quando definido como *true* , ativa o modo de segurança
do modo gráfico implementado pela classe GameLib . Este parâmetro possui valor padrão
igual a false , e deve ser usado caso a tela do jogo não seja exibida de forma correta
usando o modo padrão (que é mais eficiente quando funciona adequadamente).

A linha abaixo ilustra como executar o jogo especificando todos os 3 parâmetros opcionais
(no caso, para usar a bola implementada pela classe *DiamondBall* , determinar o intervalo
de tempo entre dois frames como 5 milissegundos e habilitar o safe mode):
```bash
$ java Pong DiamondBall 5 true
```

#### Bom jogo :)
