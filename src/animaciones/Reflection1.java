package animaciones;

import processing.core.PApplet;

public class Reflection1 extends PApplet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// All Examples Written by Casey Reas and Ben Fry
	// unless otherwise stated.
	 float baseX1, baseY1, baseX2, baseY2;
	float baseLength;
	float[] xCoords, yCoords;
	float ellipseX, ellipseY, ellipseRadius = 6;
	float directionX, directionY;
	float ellipseSpeed = (float)3.5;
	float velocityX, velocityY; 

	public void setup(){
	  size(200, 200);
	  frameRate(30);
	  fill(128);
	  smooth();
	  baseX1 = 0;
	  baseY1 = height-150;
	  baseX2 = width;
	  baseY2 = height;

	  // start ellipse at middle top of screen
	  ellipseX = width/2;
	  
	  // calculate initial random direction
	  directionX = random((float)0.1, (float)0.99);
	  directionY = random((float)0.1, (float)0.99);

	  // normalize direction vector
	  float directionVectLength = sqrt(directionX*directionX + 
	            directionY*directionY);
	  directionX /= directionVectLength;
	  directionY /= directionVectLength;
	}

	public void draw(){
	  // draw background
	  fill(0, 12);
	  noStroke();
	  rect(0, 0, width, height);

	  // calculate length of base top
	  baseLength = dist(baseX1, baseY1, baseX2, baseY2);
	  xCoords = new float[ceil(baseLength)];
	  yCoords = new float[ceil(baseLength)];

	  // fill base top coordinate array
	  for (int i=0; i<xCoords.length; i++){
	    xCoords[i] = baseX1 + ((baseX2-baseX1)/baseLength)*i;
	    yCoords[i] = baseY1 + ((baseY2-baseY1)/baseLength)*i;
	  }

	  // draw base
	  fill(200);
	  quad(baseX1, baseY1, baseX2, baseY2, baseX2, height, 0, height);

	  // calculate base top normal
	  float baseDeltaX = (baseX2-baseX1)/baseLength;
	  float baseDeltaY = (baseY2-baseY1)/baseLength;
	  float normalX = -baseDeltaY;
	  float normalY = baseDeltaX;

	  // draw ellipse
	  noFill();
	  stroke(200);
	  ellipse(ellipseX, ellipseY, ellipseRadius*2, ellipseRadius*2);

	  // calculate ellipse velocity
	  velocityX = directionX * ellipseSpeed;
	  velocityY = directionY * ellipseSpeed;

	  // move elipse
	  ellipseX += velocityX;
	  ellipseY += velocityY;

	  // normalized incidence vector
	  float incidenceVectorX = -directionX;
	  float incidenceVectorY = -directionY;

	  // detect and handle collision
	  for (int i=0; i<xCoords.length; i++){
	    // check distance between ellipse and base top coordinates
	    if (dist(ellipseX, ellipseY, xCoords[i], yCoords[i]) < ellipseRadius){

	      // calculate dot product of incident vector and base top normal 
	      float dot = incidenceVectorX*normalX + incidenceVectorY*normalY;

	      // calculate reflection vector
	      float reflectionVectorX = 2*normalX*dot - incidenceVectorX;
	      float reflectionVectorY = 2*normalY*dot - incidenceVectorY;

	      // assign reflection vector to direction vector
	      directionX = reflectionVectorX;
	      directionY = reflectionVectorY;

	      // draw base top normal at collision point
	      stroke(255, 128, 0);
	      line(ellipseX, ellipseY, ellipseX-normalX*100, 
	            ellipseY-normalY*100);
	    }
	  }

	  // detect boundary collision
	  // right
	  if (ellipseX > width-ellipseRadius){
	    ellipseX = width-ellipseRadius;
	    directionX *= -1;
	  }
	  // left 
	  if (ellipseX < ellipseRadius){
	    ellipseX = ellipseRadius;
	    directionX *= -1;
	  }
	  // top
	  if (ellipseY < ellipseRadius){
	    ellipseY = ellipseRadius;
	    directionY *= -1;
	    // randomize base top
	    baseY1 = random(height-100, height);
	    baseY2 = random(height-100, height);
	  }
	}

}
