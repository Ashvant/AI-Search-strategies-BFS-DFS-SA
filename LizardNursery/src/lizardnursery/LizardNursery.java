/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lizardnursery;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import static java.lang.Math.log;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 *
 * @author ashvant
 */
public class LizardNursery {
    
    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
         String FILENAME = "input.txt";
        
        
        FileReader fr = new FileReader(FILENAME);
        BufferedReader br = new BufferedReader(fr);
        String sCurrentLine;
        int lineNumber = 0;
        int algo = 0,nurserySize = 0,lizardCount = 0;
        int[][] nursery = null;
        int freePos = 0;
        
            while ((sCurrentLine = br.readLine()) != null)
            {       
                    if(lineNumber == 0){
                        if(sCurrentLine.equals("BFS")){
                            algo = 1;
                        }else if(sCurrentLine.equals("DFS")){
                            algo = 2;
                        }else if(sCurrentLine.equals("SA")){
                            algo = 3;
                        }
                    }else if(lineNumber == 1){
                        nurserySize = Integer.parseInt(sCurrentLine);
                        nursery = new int[nurserySize][nurserySize];
                      
                    }else if(lineNumber == 2){
                        lizardCount = Integer.parseInt(sCurrentLine);
                    }else{
                      int iterator = 0;
                      while(iterator<nurserySize){
                          nursery[lineNumber-3][iterator] = ((int)sCurrentLine.charAt(iterator)-48);
                          if(nursery[lineNumber-3][iterator]==0){
                              freePos++;
                          }
                          iterator++;             
                      }                      
                    }
                lineNumber++;
            }
            if(algo==1){
                HashMap<String,Object> resultMap = solveBFS(nursery,nurserySize,lizardCount);
                printNurseryResult(resultMap);
            }else if(algo==2){
                HashMap<String,Object> resultMap = solveBFS(nursery,nurserySize,lizardCount);
                printNurseryResult(resultMap);
            }else if(algo==3){
                 if(freePos<lizardCount){
                     String result = "FAIL";
                     HashMap<String,Object> resultMap = new HashMap<String,Object>();
                     resultMap.put("result", result);
                     printNurseryResultSA(resultMap);
                 }else{
                     HashMap<String,Object> resultMap = solveSA(nursery,nurserySize,lizardCount);
                     printNurseryResultSA(resultMap);
                 }
            }
            if (br != null)
                    br.close();

            if (fr != null)
                    fr.close();              
    }

    private static HashMap<String,Object> solveBFS(int[][] nursery, int nurserySize, int lizardCount) {
        State state = new State(nursery,-1,-1,0);
        String result = null;
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        Queue<State> queue = new LinkedList<State>();
        //Add intital state to queue
        queue.add(state);
        while(true){
            State currentState = queue.poll();
            if (currentState == null){
               result = "FAIL";
               resultMap.put("result", result);
               resultMap.put("nursery", currentState);
               return resultMap;
            }else{
               if(goalTest(currentState,lizardCount)){
                   result = "OK";
                   resultMap.put("result", result);
                   resultMap.put("nursery", currentState);
                   return resultMap;
               }else{
                   
                   if(currentState.col+1<nurserySize){
                      for(int currentRow = 0;currentRow<nurserySize;currentRow++){
                           State childState = new State(currentState);
                           if(safeHome(childState,currentRow,childState.col+1,0)){
                                childState.layout[currentRow][childState.col + 1] = 1;
                                childState.row = currentRow;
                                childState.col++;
                                childState.lizardCount++;
                                if(childState.lizardCount==lizardCount){
                                    result = "OK";
                                    resultMap.put("result", result);
                                    resultMap.put("nursery", childState);
                                }
                         
                                queue.add(childState);
                                }
                       }if(currentState.col<nurserySize-1){
                           State interState = new State(currentState.layout,currentState.row,currentState.col+1,currentState.lizardCount);
                           queue.add(interState);
                       }
                      
                   }else{   
                           State childState = new State(currentState);
                           for(int row = 0;row<nurserySize;row++){
                               for(int col=0;col<nurserySize;col++){
                                   if(safeHome(childState,row,col,1)){
                                       childState.layout[row][col] = 1;
                                       childState.lizardCount++;
                                       if(childState.lizardCount==lizardCount){
                                            result = "OK";
                                            resultMap.put("result", result);
                                            resultMap.put("nursery", childState);
                                        }
                                       queue.add(childState);
                                   }
                               }
                           }
                       }
  
                   }
               }
            }
        }
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   

    private static HashMap<String,Object> solveDFS(int[][] nursery, int nurserySize, int lizardCount) {
        State state = new State(nursery,-1,-1,0);
        String result = null;
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        Stack<State> stack = new Stack<State>();
        //Add intital state to stack
        stack.add(state);
        while(true){
            State currentState = stack.pop();
            if (currentState == null){
                result = "FAIL";
               resultMap.put("result", result);
               resultMap.put("nursery", currentState);
               return resultMap;
            }else{
               if(goalTest(currentState,lizardCount)){
                   result = "OK";
                   resultMap.put("result", result);
                   resultMap.put("nursery", currentState);
                   return resultMap;
               }else{
                   if(currentState.col+1<nurserySize){
                      for(int currentRow = 0;currentRow<nurserySize;currentRow++){
                           State childState = new State(currentState);
                           if(safeHome(childState,currentRow,childState.col+1,0)){
                           childState.layout[currentRow][childState.col + 1] = 1;
                           childState.row = currentRow;
                           childState.col++;
                           childState.lizardCount++;
                           if(childState.lizardCount==lizardCount){
                               result = "OK";
                               resultMap.put("result", result);
                               resultMap.put("nursery", childState);
                           }
                           stack.push(childState);
                           }
                       }
                      if(currentState.col<nurserySize-1){
                        State interState = new State(currentState.layout,currentState.row,currentState.col+1,currentState.lizardCount);
                        stack.push(interState);
                      }
                   }else{   
                           State childState = new State(currentState);
                           for(int row = 0;row<nurserySize;row++){
                               for(int col=0;col<nurserySize;col++){
                                   if(safeHome(childState,row,col,1)){
                                       childState.layout[row][col] = 1;
                                       childState.lizardCount++;
                                       if(childState.lizardCount==lizardCount){
                                            result = "OK";
                                            resultMap.put("result", result);
                                            resultMap.put("nursery", childState);
                                        }
                                       stack.push(childState);
                                   }
                               }
                           }
                       }
  
                   }
               }
            }
        
    }

    private static HashMap<String,Object> solveSA(int[][] nursery, int nurserySize, int lizardCount) {
        StateSA currentState = new StateSA(nursery,-1,-1,lizardCount);
        String result = null;
        HashMap<String,Object> resultMap = new HashMap<String,Object>();
        currentState = placeRandomLizards(currentState,lizardCount);
        int numberOfAttacksCurrent;
        double temperature = 1;
        double delE;
        int  i=2;
        while(true){
         
           System.out.println(temperature);
            numberOfAttacksCurrent = calcAttack(currentState);
            if(numberOfAttacksCurrent == 0){
                result = "OK";
                resultMap.put("result", result);
                resultMap.put("nursery", currentState);
                return resultMap;
            }
            if(temperature < 0.15){
             if(calcAttack(currentState)==0){
                   result = "OK";
                    resultMap.put("result", result);
                    resultMap.put("nursery", currentState);
                    return resultMap;
               }else{
                   result = "FAIL";
                   resultMap.put("result", result);
                   resultMap.put("nursery", currentState);
                   return resultMap;
               }
            }else{
                StateSA nextState = new StateSA(currentState);
                nextState = generateNextState(currentState,lizardCount);
                int numberOfAttacksNext = calcAttack(nextState);
                if(numberOfAttacksNext == 0){
                    result = "OK";
                    resultMap.put("result", result);
                    resultMap.put("nursery", nextState);
                    return resultMap;
                }
                delE = numberOfAttacksNext-numberOfAttacksCurrent;
                //System.out.println(numberOfAttacksCurrent);
               // System.out.println(numberOfAttacksNext);
                if(delE<0){
                    currentState = new StateSA(nextState);
                }else{
                    if(acceptNext(delE,temperature)){
                        currentState = new StateSA(nextState);
                    }else{
                        continue;
                    }
                }
            }
            temperature = 1/log(i);
            i++;
        }
        
    }
    
    //Calculating number of attacks in the board for simulated Annealing
     private static int calcAttack(StateSA currentState) {
         int attacks = 0;
         int i,j;
        for (int attacked = 0;attacked<currentState.lizardCount;attacked++){
            //Check left horizontal
            for (i = currentState.lizardPositionsY[attacked]-1; i >=0; i--){
                if (currentState.layout[currentState.lizardPositionsX[attacked]][i]==2){
                    break;
                }else if(currentState.layout[currentState.lizardPositionsX[attacked]][i]==1){
                    attacks++;
                    break;
                }
            }
            //Check right horizontal
        for (i = currentState.lizardPositionsY[attacked]+1; i < currentState.layout[0].length; i++){
            if (currentState.layout[currentState.lizardPositionsX[attacked]][i]==2){
                break;
            }else if(currentState.layout[currentState.lizardPositionsX[attacked]][i]==1){
                attacks++;
                break;
            }
        }
        //Check left lower diagonal
        for(i = currentState.lizardPositionsX[attacked]+1,
                j = currentState.lizardPositionsY[attacked]-1;i<currentState.layout[0].length && j>=0;i++,j--){
            if(currentState.layout[i][j]==2){
                break;
            }else if(currentState.layout[i][j]==1){
                attacks++;
                break;
            }
        }
        //Check right lower diagonal
        for(i = currentState.lizardPositionsX[attacked]+1,
                j = currentState.lizardPositionsY[attacked]+1;
                i<currentState.layout[0].length && j<currentState.layout[0].length;i++,j++){
            if(currentState.layout[i][j]==2){
                break;
            }else if(currentState.layout[i][j]==1){
                attacks++;
                break;
            }
        }
        
        //Check left upper diagonal
        for(i = currentState.lizardPositionsX[attacked]-1,j = currentState.lizardPositionsY[attacked]-1;i>=0 && j>=0;i--,j--){
            if(currentState.layout[i][j]==2){
                break;
            }else if(currentState.layout[i][j]==1){
                attacks++;
                break;
            }
        }

            //Check right upper diagonal
            for(i = currentState.lizardPositionsX[attacked]-1,
                    j = currentState.lizardPositionsY[attacked]+1;
                    i>=0 && j<currentState.layout[0].length;i--,j++){
                if(currentState.layout[i][j]==2){
                    break;
                }else if(currentState.layout[i][j]==1){
                    attacks++;
                    break;
                }
            }
        
        
        //Check vertical upper
        for(i=currentState.lizardPositionsX[attacked]-1;i>=0;i--){
            if(currentState.layout[i][currentState.lizardPositionsY[attacked]]==2){
                break;
            }else if(currentState.layout[i][currentState.lizardPositionsY[attacked]]==1){
                attacks++;
                break;
            }
        }
        //Check vertical lower
        for(i=currentState.lizardPositionsX[attacked]+1;i<currentState.layout[0].length;i++){
            if(currentState.layout[i][currentState.lizardPositionsY[attacked]]==2){
                break;
            }else if(currentState.layout[i][currentState.lizardPositionsY[attacked]]==1){
                attacks++;
                break;
            }
        }
        
        }
        return attacks;
    }
     
    //Generate next state for simulated Annealing
    private static StateSA generateNextState(StateSA currentState, int lizardCount) {
        StateSA nextState = new StateSA(currentState);
        Random rand =new Random();
        int randomLizard = rand.nextInt(lizardCount);
        int newPosX,newPosY,oldPosX,oldPosY;
        while(true){
            newPosX = rand.nextInt(nextState.layout[0].length);
            newPosY = rand.nextInt(nextState.layout[0].length);
            if(nextState.layout[newPosX][newPosY] != 1 && nextState.layout[newPosX][newPosY] != 2 ){
                oldPosX = nextState.lizardPositionsX[randomLizard];
                oldPosY = nextState.lizardPositionsY[randomLizard];
                nextState.layout[newPosX][newPosY] = 1;
                nextState.layout[oldPosX][oldPosY] = 0;
                nextState.lizardPositionsX[randomLizard] = newPosX ;
                nextState.lizardPositionsY[randomLizard] = newPosY ;
                break;
            }
        }
        nextState.lizardCount = lizardCount;
        return nextState;
    }
    //Test to accept state based on probability for simulated Annealing
    private static boolean acceptNext(double delE, double temperature) {
        double probability = Math.exp(-delE/temperature);
        double r = Math.random();
        return r<probability;
    }
    
    //place Lizards on board
     private static StateSA placeRandomLizards(StateSA state,int lizardCount) {
        int row,col,i=0;
        Random rand = new Random();
        while(i<lizardCount){
            row = rand.nextInt(state.layout[0].length);
            col = rand.nextInt(state.layout[0].length);
            if(state.layout[row][col]!=1 && state.layout[row][col]!=2){
                state.layout[row][col] = 1;
                state.lizardPositionsX[i] = row;
                state.lizardPositionsY[i] = col;
                i++;
            }
            
        }
        return state;
        
         
    }
    //Goal Test
    private static boolean goalTest(State currentState,int count) {
        if(currentState.lizardCount == count){
            return true;
        }else{
            return false;
        }
    }
    private static boolean safeHome(State state,int currentRow,int currentColumn,int flag){
        int[][] nursery = state.layout;
        int i,j;
        //Check left horizontal
        for (i = currentColumn; i >=0; i--){
            if (nursery[currentRow][i]==2){
                break;
            }else if(nursery[currentRow][i]==1){
                return false; 
            }
        }
        //Check right horizontal
        if(flag ==1){
            for (i = currentColumn; i < nursery[0].length; i++){
                if (nursery[currentRow][i]==2){
                    break;
                }else if(nursery[currentRow][i]==1){
                    return false; 
                }
            }
        }
        
        //Check left lower diagonal
        for(i = currentRow,j = currentColumn;i<nursery[0].length && j>=0;i++,j--){
            if(nursery[i][j]==2){
                break;
            }else if(nursery[i][j]==1){
                return false;
            }
        }
        //Check right lower diagonal
        if(flag ==1){
            for(i = currentRow,j = currentColumn;i<nursery[0].length && j<nursery[0].length;i++,j++){
                if(nursery[i][j]==2){
                    break;
                }else if(nursery[i][j]==1){
                    return false;
                }
            }
        }
        //Check left upper diagonal
        for(i = currentRow,j = currentColumn;i>=0 && j>=0;i--,j--){
            if(nursery[i][j]==2){
                break;
            }else if(nursery[i][j]==1){
                return false;
            }
        }
        if(flag==1){
            //Check right upper diagonal
            for(i = currentRow,j = currentColumn;i>=0 && j<nursery[0].length;i--,j++){
                if(nursery[i][j]==2){
                    break;
                }else if(nursery[i][j]==1){
                    return false;
                }
            }
        }
        
        //Check vertical upper
        for(i=currentRow;i>=0;i--){
            if(nursery[i][currentColumn]==2){
                break;
            }else if(nursery[i][currentColumn]==1){
                return false;
            }
        }
        //Check vertical lower
        for(i=currentRow;i<nursery[0].length;i++){
            if(nursery[i][currentColumn]==2){
                break;
            }else if(nursery[i][currentColumn]==1){
                return false;
            }
        }
        if(nursery[currentRow][currentColumn]==1 || nursery[currentRow][currentColumn]==2){
            return false;
        }
        return true;
        
    }
    
    private static void printNurseryResult(HashMap<String,Object> resultMap){
        String result = (String)resultMap.get("result");
        State state = (State)resultMap.get("nursery");
        Writer writer = null;
        int iterator = 0,iterator2=0;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream("output.txt"), "utf-8"));
            writer.write(result);
            if(result.equals("OK")){
                int[][] nursery = state.layout;
                writer.write("\n");
            for(iterator =0;iterator<nursery.length;iterator++){
                for(iterator2 = 0;iterator2<nursery[iterator].length;iterator2++){
                    writer.write(String.valueOf(nursery[iterator][iterator2]));
                }
                writer.write("\n");
              }
            }
            
        } catch (IOException ex) {
          // report
        } finally {
           try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
    }
    private static void printNurseryResultSA(HashMap<String,Object> resultMap){
        String result = (String)resultMap.get("result");
        StateSA state = (StateSA)resultMap.get("nursery");
        Writer writer = null;
        int iterator = 0,iterator2=0;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream("output.txt"), "utf-8"));
            writer.write(result);
            if(result.equals("OK")){
                int[][] nursery = state.layout;
                writer.write("\n");
            for(iterator =0;iterator<nursery.length;iterator++){
                for(iterator2 = 0;iterator2<nursery[iterator].length;iterator2++){
                    writer.write(String.valueOf(nursery[iterator][iterator2]));
                }
                writer.write("\n");
              }
            }
            
        } catch (IOException ex) {
          // report
        } finally {
           try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
    }

}
class State{
    int[][] layout;
    int row;
    int col;
    int lizardCount;
    int[] lizardPositionsX;
    int[] lizardPositionsY;
    public State(int[][] nursery,int rowsent,int colsent,int count){
        layout = new int[nursery[0].length][nursery[0].length];
        for (int i=0; i < nursery[0].length; i++) {
            System.arraycopy(nursery[i], 0, layout[i], 0, nursery[0].length);
        }
        row = rowsent;
        col = colsent;
        lizardCount = count;
    }
    public State(State state){
        layout = new int[state.layout[0].length][state.layout[0].length];
        for (int i = 0; i < state.layout.length; i++) {
            System.arraycopy(state.layout[i], 0, layout[i], 0, state.layout.length);
        }
        row = state.row;
        col = state.col;
        lizardCount = state.lizardCount;
    }
}
class StateSA{
    int[][] layout;
    int row;
    int col;
    int lizardCount;
    int[] lizardPositionsX;
    int[] lizardPositionsY;
    public StateSA(int[][] nursery,int rowsent,int colsent,int count){
        layout = new int[nursery[0].length][nursery[0].length];
        for (int i=0; i < nursery[0].length; i++) {
            System.arraycopy(nursery[i], 0, layout[i], 0, nursery[0].length);
        }
        row = rowsent;
        col = colsent;
        lizardCount = count;
        lizardPositionsX = new int[count];
        lizardPositionsY = new int[count];
        
    }
    public StateSA(StateSA state){
        layout = new int[state.layout[0].length][state.layout[0].length];
        for (int i = 0; i < state.layout.length; i++) {
            System.arraycopy(state.layout[i], 0, layout[i], 0, state.layout.length);
        }
        lizardCount = state.lizardCount;
        lizardPositionsX = new int[state.lizardCount];
        lizardPositionsY = new int[state.lizardCount];
        System.arraycopy(state.lizardPositionsX, 0, lizardPositionsX, 0, state.lizardPositionsX.length);
        System.arraycopy(state.lizardPositionsY, 0, lizardPositionsY, 0, state.lizardPositionsY.length);
    }
}