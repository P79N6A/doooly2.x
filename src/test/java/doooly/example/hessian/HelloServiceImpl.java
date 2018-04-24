package doooly.example.hessian;


public class HelloServiceImpl implements HelloService {

    public String helloWorld(String message){
        return "hello," + message;
    }

}