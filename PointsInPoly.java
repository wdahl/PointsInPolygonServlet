package csi403;

// Import required java libraries
import java.io.*;
import java.lang.*;
import java.lang.Object;
import java.awt.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;
import java.awt.Point;

// Extend HttpServlet class
public class PointsInPoly extends HttpServlet {
  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
	 
	      doService(request, response); 
	      
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }

 public static int Max(int temp[]){
	 //returns the max number from the given array
	  int max=temp[0];
	  for(int r=0; r<temp.length;r++){
		  if(temp[r]>max){
			  max=temp[r];
		  }
	  }
	  return max;
  }
 public static int Min(int temp[]){
	//returns the min number from the given array
	  int min=temp[0];
	  for(int s=0; s<temp.length;s++){
		  if(temp[s]<min){
			  min=temp[s];
		  }
	  }
	  return min;
 }

   
  private void doService(HttpServletRequest request,
		  HttpServletResponse response)
            throws ServletException, IOException
  {
	  try{
	//Write code
      // Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }
      
      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);
      // Get the singular JSON object (name:value pair) in this message.    
      JsonObject obj = reader.readObject();
	  
	  response.setContentType("application/json");
	  PrintWriter out = response.getWriter();

      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");
	  
	  if(inArray.size() < 3)
		{
		  out.println("Not enough Points");
		  return;	  
		}
     
      int ans=0;
      int gridSize=19;//fixed in this hw
      int X[]= new int[inArray.size()];
      int Y[]= new int[inArray.size()];
     
      
      //get x and y values from inList
      for (int i = 0; i < inArray.size(); i++) {
      	JsonObject abc = inArray.getJsonObject(i);
      	X[i]=abc.getInt("x");
      	Y[i]=abc.getInt("y");	
      }
      
      //Create a polygon with the info from Json
      Polygon poly= new Polygon(X,Y,X.length);
      //find the min and max values for X and Y
      int Xmin=Min(X);
      int Xmax=Max(X);
      int Ymin=Min(Y);
      int Ymax=Max(Y);
	  
	  if(Xmin < 0 || Ymin < 0 || Xmax >18 || Ymax > 18)
	  {
		  out.println("Out of bounds, grid is 19x19 (0-18)");
		  return;
	  }
		
		double k = 0;
		double l = 0;
    
      for (int i = 0; i < gridSize; i++) 
      {
    	  for (int j = 0; j < gridSize; j++) 
    	  {
			  if(poly.contains(i, j) && j>(Ymin+1))
			  {
				  k = j + .01;
				  l = i;
			  }
			  else
			  {
				  k = j;
				  l = i;
			  }
    		  if(poly.contains(l, k)){
    			  if((i<Xmax)&&(i>Xmin)){
    				  if((j<Ymax)&&(j>Ymin)){
    					  ans++;  
						}
    			  }
    		  }
    	  }
    }
    
    
    //set up a Json output with ans
    JsonObject Output = Json.createObjectBuilder()
    		.add("count", ans)
    		.build();    
      	
      out.println(Output);  
	  }	  
	  catch(Exception e)
	  {
	    	  //error handling
	    	  response.setContentType("application/json");
	          PrintWriter out = response.getWriter();
	          out.println("Malformed JSON");      	  
	   }
  }
  
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }
}