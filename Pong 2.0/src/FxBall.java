import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;


public class FxBall extends Ball {
    private int fxLength = 170;
    private LinkedList<Double> positionCoords = new LinkedList<Double>();

    /**
    Construtor da classe DiamondBall. Recebe o mesmo conjunto de parâmetros que o construtor da superclasse.

        @param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
        @param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
        @param width largura do retangulo que representa a bola.
        @param height altura do retangulo que representa a bola.
        @param color cor da bola.
        @param speed velocidade da bola (em pixels por millisegundo).
        @param vx componente x do vetor unitário (normalizado) que representa a direção da bola.
        @param vy componente y do vetor unitário (normalizado) que representa a direção da bola.
    */

    public FxBall(double cx, double cy, double width, double height, Color color, double speed, double vx, double vy){

		super(cx, cy, width, height, color, speed, vx, vy);
    }
    
    public void draw() {
        int count = 0;
        int index = 0;
        Color cor = getColor();
        double width = getWidth();
        double height = getHeight();

        int red = cor.getRed();
        int green = cor.getGreen();
        int blue = cor.getBlue();
        int alpha = cor.getAlpha();

        for(Iterator<Double> it = positionCoords.iterator(); it.hasNext();) {
            if(index % 2 == 0) {
                if(index != 0 && alpha >= 0) {
                    if(index == 2) alpha = 25;
                    cor = new Color(red, green, blue, alpha);
                    if(count % 5 == 0) alpha--;
                }

                GameLib.setColor(cor);
                if(index <= positionCoords.size() - 2) {
                    GameLib.fillRect(positionCoords.get(index), positionCoords.get(index + 1), width, height);
                }
                    
                width *= 0.99;
                height *= 0.99;
            }

            count++;
            index++;
            if(index > positionCoords.size()) break;
        }
    }
    
    public void update(long delta) {
    
        positionCoords.addFirst(getCy());
        positionCoords.addFirst(getCx());

        if(positionCoords.size() > fxLength) {
            positionCoords.removeLast();
            positionCoords.removeLast();
        } 
        
        super.update(delta);
    }
}