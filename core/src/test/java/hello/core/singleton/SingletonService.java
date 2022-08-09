package hello.core.singleton;

public class SingletonService {
    
    private static final SingletonService instance = new SingletonService();
    
    public static SingletonService getInstance(){
        return instance;
    }

    private SingletonService(){ //new로 SingletonService를 생성 못하게 막아놓음
    } // 생성자를 private로 막아놓으면 외부에서 new로 생성 못함
    
    public void losic(){
        System.out.println("싱글톤 객체 로직 호출");
    }
    
}
