//package function;
//
//import java.util.Set;
//import java.util.TreeSet;
//
//public class GetNumberOfAnswersVisitor implements ExpressionVisitor{
//    int numAnswers;
//    
//    public GetNumberOfAnswersVisitor(){
//        this.numAnswers = 0;
//    }
//    
//    public int getNumAnswers() {
//        return numAnswers;
//    }
//
//    ////////untested
//    //call after variable visits expression
//    @Override
//    public void visit(Expression expression) {
//        if(expression instanceof ExpressionGroup){
//            numAnswers = 0;
//            for(int i=0;i<expression.getNumberOfChildren();i++){
//                GetNumberOfAnswersVisitor childVisitor = new GetNumberOfAnswersVisitor();
//                expression.getChild(i).getVisitedBy(childVisitor);
//                numAnswers += childVisitor.getNumAnswers();
//            }
//        }else{
//            numAnswers = 1;
//            for(int i=0;i<expression.getNumberOfChildren();i++){
//                GetNumberOfAnswersVisitor childVisitor = new GetNumberOfAnswersVisitor();
//                expression.getChild(i).getVisitedBy(childVisitor);
//                numAnswers *= childVisitor.getNumAnswers();
//            }
//        }
//    }
//}
