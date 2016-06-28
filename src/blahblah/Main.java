/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blahblah;

import java.awt.Color;
import simplegui.*;
/**
 *
 * @author Christian Prajzner
 */
public class Main implements GUIListener, TimerListener, SGMouseListener {

    SimpleGUI sg = new SimpleGUI();
    final int DELTA = 1;  //just a helper variable meaning a small space, would have used dx or dy but its already used for someting else
    final int WIDTH = sg.getWidth();
    final int HEIGHT  = sg.getHeight();
    static final int BALLWIDTH = 10;
    double dx ;
    double dy ;
    double ballXPos = WIDTH/2;
    double ballYPos = HEIGHT/2;
    AbstractDrawable ball, player, computer;
    double prevX = 0;
    double prevY = 0;
    double SPEED_DIFFICULTY = 4;//don't put above 10 or things mess up
    int playerPosition = 0;
    int playerWidth = 100, computerWidth =  100;
    int timeDifficulty = 4;
    /* READ ME */
    /* TO PLAY FOR HIGH SCORE, SET COMPUTERSKILL TO 1 AND REMOVE DECREMENT IN REACT TO TIMER METHOD */
    double computerSkill = 1; // between 0 and 1 smaller value will mean easier game because we want to something like if (math.random() > computerSucess) then computer will go to appropriate position
    boolean computerMoved = false;
    int score = 0;
    int playerXPos, playerYPos;
    int overallDifficulty = (int) SPEED_DIFFICULTY/timeDifficulty;
    
            
    
    
    
    Main (){
        sg.setBackgroundColor(Color.black);
        sg.labelButton1("RESET");
        sg.eraseAllDrawables();
        sg.drawFilledBox(WIDTH/2, HEIGHT - 10, playerWidth, 5, Color.red, 1, "player");  
        sg.drawFilledBox(WIDTH/2, 10, computerWidth, 5, Color.blue, 1, "computer");

        //sg.drawFilledEllipse(WIDTH/2, HEIGHT/2, BALLWIDTH, BALLWIDTH, Color.BLACK, 1, "ball");
        this.setXY();
        sg.setAutoRepaint(false);
        sg.registerToMouse(this);
        sg.registerToGUI(this);
        sg.registerToTimer(this);
        sg.timerStart(timeDifficulty);
        sg.timerPause();
        
    }
    
    private static double convertDegreeToRadian(double d){
        
        return ((d * 2 * Math.PI)/360);
    }
    
    
    
    private static double getRandomAngle(){
        double degree;
        //do while loop for playable values
        do{
            degree = (360 * (Math.random()));
        } while ((degree >120 && degree< 240) || (degree> 300) || degree<60 || (degree < 280 && degree > 260) || (degree <100 && degree > 80));
        degree = convertDegreeToRadian(degree);
        return degree;
    }
    
    
    private void setXY(){
        //need a random dx and a random dy for the intial direction
        //actuall need an intial degree
        
        //cosine and sine functions in RADIAN
        
        //getRandomangle shoud be between 0 and 2pi
        double degree = getRandomAngle();
        double xMove = Math.cos(degree);
        double yMove = Math.sin(degree);
        
        //we always want the ball to start upwards so to give the user some
        //reaction time, so we want an angle that will give a negative value for
        //its sine function (negative becasue simple gui works backwards)
        if(yMove > 0)
            yMove = -yMove;
        dx = SPEED_DIFFICULTY*xMove; dy = SPEED_DIFFICULTY*yMove; 
        System.out.println("x  is "  + dx +  " y is " + dy);
        
        
    }

    @Override
    public void reactToButton1() {
        new Main();
        computerSkill =1;
    }

    @Override
    public void reactToButton2() {
    }

    @Override
    public void reactToSwitch(boolean bln) {
    }

    @Override
    public void reactToSlider(int i) {
        /*how to get a null pointer exception decomment the // comments 
         * in this method.  My hypothesis:  I think because the method
         * react to timer needs player.PosX and player.PosY there 
         * is a moment in the game where the player box is erased but not yet drawn
         * and in between that time the react to timer method is trying to access
         * it's x and y positions, i fixed it so that the "player" box
         * is never actually erased, but it's just an interesting question
         * about callback methods in general.  I should probably ask about it.
         */
        //sg.eraseAllDrawables("player");
        sg.timerStart(timeDifficulty);
        playerPosition = i*(WIDTH - playerWidth)/100;
        sg.animMoveTo(playerPosition, HEIGHT - 10, "player");
        //sg.drawFilledBox(playerPosition, HEIGHT - 10, playerWidth, 5, Color.red, 1, "player"); 
    }

    private boolean shouldWeRedrawTheComputer(){
        return (ballYPos < 30 && !computerMoved);
    }
    
    
    @Override
    public void reactToTimer(long l) {
        computer = sg.getDrawable("computer");
        if(shouldWeRedrawTheComputer()){
            sg.eraseAllDrawables("computer");
            boolean computerSuccess = (Math.random() < computerSkill);
            int computerXPos;
            if(computerSuccess){
                //computerXPos = (int)(((Math.random())* 2 * computerWidth) + ballXPos - computerWidth);
                computerXPos =(int)(ballXPos - computerWidth/2);
                sg.drawFilledBox(computerXPos, 10, computerWidth, 5, Color.blue, 1, "computer");
            }
            else{ //failure
                if(ballXPos > WIDTH/2) //have it appear on 
                    computerXPos = (int)(ballXPos - computerWidth -DELTA);//computerXPos = (int)(((Math.random())* 2 * computerWidth) + ballXPos - 4* computerWidth);
                else
                    computerXPos = (int)(ballXPos + DELTA) ;//computerXPos = (int)(((Math.random())* 2 * computerWidth) + ballXPos);
                sg.drawFilledBox(computerXPos, 10, computerWidth, 5, Color.blue, 1, "computer");
            }
            computerMoved = true;
            
        }
        //System.out.println("" + xy[0] + "  " + xy[1]);
        sg.eraseAllDrawables("ball");
        sg.drawFilledEllipse(ballXPos, ballYPos, BALLWIDTH, BALLWIDTH, Color.WHITE, 1, "ball");
        
        ball = sg.getDrawable("ball");
        player = sg.getDrawable("player");        
        playerXPos = player.posX;
        playerYPos = player.posY;
        //System.out.println("the value of x pos is ");
        //player = null;
        //System.out.println("previous x value " + prevX);
        //System.out.println("current x value " + ballXPos);
        double tempX, tempY;
        if(prevX == 0){
           tempX = prevX = ball.posX;
        }
        else
            tempX = prevX = prevX + dx;
            
        ballXPos = tempX + dx;
        
        //System.out.println("previous x value " + prevX);
        //System.out.println("current x value " + ballXPos);
        
        
        if(prevY == 0){
            tempY = prevY = ball.posY;
        }
        else
            tempY = prevY = prevY + dy;
        
        
        ballYPos = tempY + dy;
        //System.out.println("previous dy value " + prevY);
        //System.out.println("current dy value " + ballYPos);
        
        
        //don't need tto set ball = null everytime becaue only getting it once
        //ball = null;
        boolean gameOver = false;
       
        
        //make a series of if else statments for dx pos
        
        if((ballXPos >= WIDTH - BALLWIDTH) || ballXPos <= 0){
            dx = -(dx);

        }

        
        else if (ball.posY <= 0) {
            gameOver = true;
            sg.print("you won: score: " + score); 
        }
        else if (ball.posY + BALLWIDTH >= HEIGHT ){
            gameOver = true;
            sg.print("you lost.  score: " + score);
        }
        
        //we want to reserve this statement for when it hits the player or computer
        //had to make two differnt ones to keep track of score and movements etc
        else if (ballYPos <= computer.posY && ballXPos < (computer.posX + computerWidth) && ballXPos > computer.posX) {
            dy = -dy;
        }
        else if (((ballYPos + BALLWIDTH) >= playerYPos) && ballXPos < (playerXPos + playerWidth) && ballXPos > playerXPos){
            dy = -dy;
            computerMoved = false;
            sg.print(++score + "");
            computerSkill -= 0.01;
            
        }
        
        /* if ball hits the player  or the computer
        else if (ball.posY + BALLWIDTH >= player.posY || ball.posY <= computer.posY){
            xy[1] = - ( xy[1]);
        }*/
        /*
        else
            ;
        */
        if(gameOver)
            sg.removeFromTimer(this);
        
        //sg.animMoveAllRel(dx, dy, "ball");
        
        sg.repaintPanel();
        
        
        
            
        //--------------------try another way----------------------------------    
        
        //sg.drawFilledEllipse(ballXPos, ballYPos, BALLWIDTH, BALLWIDTH, Color.BLACK, 1, "ball");
        
        
    }
    

    @Override
    public void reactToMouseClick(int x , int y) {
        playerPosition = x;
        sg.animMoveTo(playerPosition, HEIGHT - 10, "player");
        
    }
    
    public static void main(String[] args) {
        new Main();
    }
}
