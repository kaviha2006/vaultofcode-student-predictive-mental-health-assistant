import java.util.ArrayList;
import java.util.Scanner;

/* =========================
   TASK ENTITY
   ========================= */
class Task{
    private String name;
    private boolean completed;

    Task(String name){
        this.name=name;
        this.completed=false;
    }

    public String getName(){
        return name;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void markCompleted(){
        completed=true;
    }
}

/* =========================
   WELLNESS STATE
   ========================= */
enum WellnessState{
    OPTIMAL,
    WARNING,
    BURNOUT_RISK
}

/* =========================
   PRODUCTIVITY ENGINE
   ========================= */
class ProductivityEngine{
    private ArrayList<Task> tasks=new ArrayList<>();
    private ArrayList<Double> productivityHistory=new ArrayList<>();

    public void addTask(String name){
        tasks.add(new Task(name));
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public int totalTasks(){
        return tasks.size();
    }

    public int completedTasks(){
        int count=0;
        for(Task t:tasks){
            if(t.isCompleted())count++;
        }
        return count;
    }

    public int pendingTasks(){
        return totalTasks()-completedTasks();
    }

    public double productivityScore(){
        if(tasks.isEmpty())return 0;
        double score=(completedTasks()*100.0)/tasks.size();
        productivityHistory.add(score);
        return score;
    }

    public double averageProductivity(){
        if(productivityHistory.isEmpty())return 0;
        double sum=0;
        for(double p:productivityHistory){
            sum+=p;
        }
        return sum/productivityHistory.size();
    }
}

/* =========================
   WELLNESS ANALYZER
   ========================= */
class WellnessAnalyzer{

    public static int burnoutRiskScore(
        int studyHours,
        int sleepHours,
        double productivity,
        int pendingTasks
    ){
        int score=0;

        if(studyHours>9)score+=25;
        if(sleepHours<6)score+=25;
        if(productivity<50)score+=25;
        if(pendingTasks>5)score+=25;

        return score;
    }

    public static WellnessState evaluateState(int riskScore){
        if(riskScore>=75)return WellnessState.BURNOUT_RISK;
        if(riskScore>=50)return WellnessState.WARNING;
        return WellnessState.OPTIMAL;
    }

    public static void adaptiveSuggestions(
        WellnessState state,
        int studyHours,
        int sleepHours,
        double productivity
    ){
        System.out.println("\n--- Adaptive Suggestions ---");

        if(state==WellnessState.BURNOUT_RISK){
            System.out.println("Burnout risk is HIGH.");
            if(studyHours>9)
                System.out.println("- Reduce daily study hours.");
            if(sleepHours<6)
                System.out.println("- Improve sleep routine.");
            if(productivity<50)
                System.out.println("- Reduce workload and focus on priorities.");
        }
        else if(state==WellnessState.WARNING){
            System.out.println("Stress indicators detected.");
            System.out.println("- Improve time management.");
            System.out.println("- Take regular breaks.");
        }
        else{
            System.out.println("You are maintaining a healthy balance.");
            System.out.println("- Continue current study habits.");
        }
    }
}

/* =========================
   MAIN APPLICATION
   ========================= */
public class StudentAssistantApp{

    static Scanner sc=new Scanner(System.in);
    static ProductivityEngine engine=new ProductivityEngine();

    public static void addTask(){
        System.out.print("Enter study task: ");
        engine.addTask(sc.nextLine());
        System.out.println("Task added successfully.");
    }

    public static void completeTask(){
        viewTasks();
        if(engine.totalTasks()==0)return;

        System.out.print("Enter task number to mark completed: ");
        int index=sc.nextInt();
        sc.nextLine();

        if(index<1||index>engine.totalTasks()){
            System.out.println("Invalid task number.");
            return;
        }
        engine.getTasks().get(index-1).markCompleted();
        System.out.println("Task marked completed.");
    }

    public static void viewTasks(){
        if(engine.totalTasks()==0){
            System.out.println("No tasks available.");
            return;
        }
        System.out.println("\n--- Study Tasks ---");
        for(int i=0;i<engine.getTasks().size();i++){
            Task t=engine.getTasks().get(i);
            System.out.println((i+1)+". "+t.getName()+
                (t.isCompleted()?" [Completed]":" [Pending]"));
        }
    }

    public static void wellnessAnalysis(){
        System.out.print("Enter study hours today: ");
        int study=sc.nextInt();
        System.out.print("Enter sleep hours last night: ");
        int sleep=sc.nextInt();
        sc.nextLine();

        double productivity=engine.productivityScore();
        int pending=engine.pendingTasks();

        int riskScore=WellnessAnalyzer.burnoutRiskScore(
            study,sleep,productivity,pending
        );

        WellnessState state=WellnessAnalyzer.evaluateState(riskScore);

        System.out.println("\nProductivity Score: "+productivity+"%");
        System.out.println("Burnout Risk Score: "+riskScore+"%");
        System.out.println("Wellness State: "+state);

        WellnessAnalyzer.adaptiveSuggestions(
            state,study,sleep,productivity
        );
    }

    public static void summaryReport(){
        System.out.println("\n--- Summary Report ---");
        System.out.println("Total Tasks: "+engine.totalTasks());
        System.out.println("Completed Tasks: "+engine.completedTasks());
        System.out.println("Pending Tasks: "+engine.pendingTasks());
        System.out.println("Average Productivity: "+
            engine.averageProductivity()+"%");
    }

    public static void main(String[] args){
        int choice;
        do{
            System.out.println("\n===== STUDENT PRODUCTIVITY & WELLNESS ASSISTANT =====");
            System.out.println("1. Add Study Task");
            System.out.println("2. Complete Task");
            System.out.println("3. View Tasks");
            System.out.println("4. Wellness Analysis");
            System.out.println("5. Summary Report");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            choice=sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:addTask();break;
                case 2:completeTask();break;
                case 3:viewTasks();break;
                case 4:wellnessAnalysis();break;
                case 5:summaryReport();break;
                case 6:System.out.println("Exiting application...");break;
                default:System.out.println("Invalid choice.");
            }
        }while(choice!=6);
    }
}
