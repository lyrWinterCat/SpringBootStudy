package hello.core.singleton;

public class StatefulService {
//    private int price; //상태를 유지하는 필드
    
/*    public void order(String name, int price){ >>잘못됨
        System.out.println("name = " + name+" price = "+price);
        this.price = price;
    }*/   
    public int order(String name, int price){
        System.out.println("name = " + name+" price = "+price);
 //       this.price = price;
        return price;
    }

/*    public void getPrice(){
        return price;
    }*/


}
