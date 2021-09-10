import java.awt.Color;
import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
	Classe que gerencia uma ou mais bolas presentes em uma partida. Esta classe é a responsável por instanciar 
	e gerenciar a bola principal do jogo (aquela que existe desde o ínicio de uma partida), assim como eventuais 
	bolas extras que apareçam no decorrer da partida. Esta classe também deve gerenciar a interação da(s) bola(s)
	com os alvos, bem como a aplicação dos efeitos produzidos para cada tipo de alvo atingido.
*/

public class BallManager {
	private double initSpeed;
	private long startBoost;
	private long endBoost;
	private int index;
	private List<IBall> ballsList = new LinkedList<IBall>();
	private List<Long> boostList = new LinkedList<Long>();
	private List<Long> creationTime = new LinkedList<Long>();
	private List<IBall> auxDuplicator = new LinkedList<IBall>();
	private Random random = new Random(System.currentTimeMillis());

	/**
		Atributo privado que representa a bola principal do jogo.	
	*/

	private IBall theBall = null;

	/**
		Atributo privado que representa o tipo (classe) das instâncias de bola que serão criadas por esta classe.
	*/

	private Class<?> ballClass = null;

	/**
		Construtor da classe BallManager.
		
		@param className nome da classe que define o tipo das instâncias de bola que serão criadas por esta classe. 
	*/

	public BallManager(String className){

		try{
			ballClass = Class.forName(className);
		}
		catch(Exception e){

			System.out.println("Classe '" + className + "' não reconhecida... Usando 'Ball' como classe padrão.");
			ballClass = Ball.class;
		}
	}

	/**
		Recebe as componetes x e y de um vetor, e devolve as componentes x e y do vetor normalizado (isto é, com comprimento igual a 1.0).
	
		@param x componente x de um vetor que representa uma direção.
		@param y componente y de um vetor que represetna uma direção.

		@return array contendo dois valores double que representam as componentes x (índice 0) e y (índice 1) do vetor normalizado (unitário).
	*/
	private double [] normalize(double x, double y){

		double length = Math.sqrt(x * x + y * y);

		return new double [] { x / length, y / length };
	}
	
	/**
		Cria uma instancia de bola, a partir do tipo (classe) cujo nome foi passado ao construtor desta classe.
		O vetor direção definido por (vx, vy) não precisa estar normalizado. A implemntação do método se encarrega
		de fazer a normalização.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
		@param vx componente x do vetor (não precisa ser unitário) que representa a direção da bola.
		@param vy componente y do vetor (não precisa ser unitário) que representa a direção da bola.
	*/

	private IBall createBallInstance(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		IBall ball = null;
		double [] v = normalize(vx, vy);

		try{
			Constructor<?> constructor = ballClass.getConstructors()[0];
			ball = (IBall) constructor.newInstance(cx, cy, width, height, color, speed, v[0], v[1]);
		}
		catch(Exception e){

			System.out.println("Falha na instanciação da bola do tipo '" + ballClass.getName() + "' ... Instanciando bola do tipo 'Ball'");
			ball = new Ball(cx, cy, width, height, color, speed, v[0], v[1]);
		}

		initSpeed = speed;
		return ball;
	} 

	/**
		Cria a bola principal do jogo. Este método é chamado pela classe Pong, que contem uma instância de BallManager.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
		@param vx componente x do vetor (não precisa ser unitário) que representa a direção da bola.
		@param vy componente y do vetor (não precisa ser unitário) que representa a direção da bola.
	*/

	public void initMainBall(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		theBall = createBallInstance(cx, cy, width, height, color, speed, vx, vy);
	}

	/**
		Método que desenha todas as bolas gerenciadas pela instância de BallManager.
		Chamado sempre que a(s) bola(s) precisa ser (re)desenhada(s).
	*/

	public void draw(){

		theBall.draw();

		for(IBall ball : ballsList){
            ball.draw();
        }
	}
	
	/**
		Método que atualiza todas as bolas gerenciadas pela instância de BallManager, em decorrência da passagem do tempo.
		
		@param delta quantidade de millisegundos que se passou entre o ciclo anterior de atualização do jogo e o atual.
	*/

	public void update(long delta){
	
		theBall.update(delta);

		for(IBall ball : ballsList){
            ball.update(delta);
        }

		endBoost = System.currentTimeMillis();

		if(endBoost - startBoost >= BoostTarget.BOOST_DURATION) theBall.setSpeed(initSpeed);

		for(IBall ball : ballsList){
        	if(endBoost - boostList.get(ballsList.indexOf(ball)) >= BoostTarget.BOOST_DURATION) {
				ball.setSpeed(initSpeed);
			}
		}

		index = 0;

		for(Iterator<Long> it = creationTime.iterator(); it.hasNext();) {
            if((endBoost - it.next()) >= DuplicatorTarget.EXTRA_BALL_DURATION){
				it.remove();
				ballsList.remove(index);
			 	boostList.remove(index);
            }
			index++;
		}
	}
	
	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com uma parede.

		@param wall referência para uma instância de Wall para a qual será verificada a ocorrência de colisões.
		@return um valor int que indica quantas bolas colidiram com a parede (uma vez que é possível que mais de 
		uma bola tenha entrado em contato com a parede ao mesmo tempo).
	*/

	public int checkCollision(Wall wall){

		int hits = 0;

		if(theBall.checkCollision(wall)) hits++;

		for(IBall ball : ballsList){
            if(ball.checkCollision(wall)) hits++;
        }

		return hits;
	}

	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com um player.

		@param player referência para uma instância de Player para a qual será verificada a ocorrência de colisões.
	*/
	
	public void checkCollision(Player player){

		theBall.checkCollision(player);

		for(IBall ball : ballsList){
            ball.checkCollision(player);
        }
	}

	/**
		Método que processa as colisões entre as bolas gerenciadas pela instância de BallManager com um alvo.

		@param target referência para uma instância de Target para a qual será verificada a ocorrência de colisões.
	*/



	public void checkCollision(Target target){

		if(theBall.checkCollision(target)) {
			if(target instanceof BoostTarget) {
				if(theBall.getSpeed() <= initSpeed) {
					double newSpeed = BoostTarget.BOOST_FACTOR * theBall.getSpeed();
					theBall.setSpeed(newSpeed);
					startBoost = System.currentTimeMillis();
				}

			} else if(target instanceof DuplicatorTarget) {
				IBall newBall = createBallInstance(theBall.getCx(), theBall.getCy(), theBall.getWidth(), theBall.getHeight(), Color.RED, initSpeed, random.nextInt(), random.nextInt());
				ballsList.add(newBall);

				Long newTime = Long.valueOf(0);
				boostList.add(newTime);

				Long initTime = System.currentTimeMillis();
				creationTime.add(initTime);
			}
		}

		for(IBall ball : ballsList) {
            if(ball.checkCollision(target)) {
				if(target instanceof BoostTarget) {
					if(ball.getSpeed() <= initSpeed) {
						double newSpeed = BoostTarget.BOOST_FACTOR * ball.getSpeed();
						ball.setSpeed(newSpeed);
						boostList.set(ballsList.indexOf(ball), System.currentTimeMillis());
					}
				}

				if(target instanceof DuplicatorTarget) {
					IBall newBall = createBallInstance(target.getCx(), target.getCy(), theBall.getWidth(), theBall.getHeight(), Color.RED, initSpeed, random.nextInt(), random.nextInt());
					auxDuplicator.add(newBall);

					Long newTime = Long.valueOf(0);
					boostList.add(newTime);

					Long initTime = System.currentTimeMillis();
					creationTime.add(initTime);
				}
			}
		}

		ballsList.addAll(auxDuplicator);
		auxDuplicator.clear();
	}
}


