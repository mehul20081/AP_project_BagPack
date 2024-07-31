package com.company;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;
interface User{
    default void view_lec_material(Course course){
        for(Lecture_material lec_mat: course.get_Lec_mat()){
            lec_mat.view();
        }
//        for(Lec_slides lec_slide:course.get_Lec_slides()){
//            lec_slide.view();
//        }
//        for(Lec_vid lec_vid:course.get_Lec_vids()){
//            lec_vid.view();
//        }

    }
    default void view_assessments(Course course){
        for(int i=0;i<course.get_assessments().size();i++) {
            System.out.print("ID: "+Integer.toString(i)+" ");
            course.get_assessments().get(i).view();
            System.out.println("----------------");
        }
    }
    default void view_comments(){
        Discussion_Form.view_comments();
    }
    default void add_comments(){
        System.out.print("Enter comment: ");
        Scanner sc=new Scanner(System.in);
        String com=sc.nextLine();
        Discussion_Form.add_comments(this,com);
    }
    default void logout(Bagpack bag){
        bag.run();
    }
    String get_name();
}

class Professor implements User{
    private String prof_name;
    private Course course;

    public Professor(String name,Course course){
        this.prof_name=name;
        this.course=course;
    }
    public String get_name(){
        return prof_name;
    }
    public void run(Bagpack bag){
        System.out.println("Welcome "+prof_name);
        System.out.println("INSTRUCTOR MENU\n" +
                "1. Add class material\n" +
                "2. Add assessments\n" +
                "3. View lecture materials\n" +
                "4. View assessments\n" +
                "5. Grade assessments\n" +
                "6. Close assessment\n" +
                "7. View comments\n" +
                "8. Add comments\n" +
                "9. Logout");
        Scanner sc=new Scanner(System.in);
        int in=sc.nextInt();
        if(in==1){
            System.out.println("1. Add Lecture Slide\n" +
                    "2. Add Lecture Video");
            int ind=sc.nextInt();
            if(ind==1){
                Scanner scan=new Scanner(System.in);
                System.out.print("Enter topic of slides: ");
                String topic= scan.nextLine();
                System.out.print("Enter number of slides: ");
                int num_slides=sc.nextInt();
                System.out.println("Enter content of slides");
                ArrayList<String> content=new ArrayList<>();
                for(int i=0;i<num_slides;i++) {
                    System.out.print("Content of slide "+Integer.toString(i+1)+": ");
                    String temp=scan.nextLine();
                    content.add(temp);
                }
                Lecture_material lec=new Lec_slides(topic,num_slides,content,prof_name);
                course.add_Lec_mat(lec);
            }
            else{
                Scanner scan=new Scanner(System.in);
                System.out.print("Enter topic of video: ");
                String topic=scan.nextLine();
                System.out.print("Enter filename of video: ");
                String name=scan.nextLine();
                if(name.endsWith(".mp4")){
                    Lecture_material lec=new Lec_vid(topic,name,prof_name);
                    course.add_Lec_mat(lec);
                }
                else{
                    System.out.println("filename must have .mp4 extension!");
                }
            }
            run(bag);
        }
        if(in==2){
            System.out.println("1. Add Assignment\n" +
                    "2. Add Quiz");
            int in_2=sc.nextInt();
            if(in_2==1){
                Scanner scan=new Scanner(System.in);
                System.out.print("Enter problem statement: ");
                String ques=scan.nextLine();
                System.out.print("Enter max marks: ");
                int max_marks=sc.nextInt();
                Assessments assessment=new Assignments(ques,max_marks);
                course.add_assessments(assessment);
            }
            else{
                Scanner scan=new Scanner(System.in);
                System.out.print("Enter quiz question: ");
                String s=scan.nextLine();
                Assessments assessment=new Quizzes(s);
                course.add_assessments(assessment);
            }
            run(bag);
        }
        if(in==3){
            view_lec_material(course);
            run(bag);
        }
        if(in==4){
            view_assessments(course);
            run(bag);
        }
        if(in==5){
            System.out.println("List of assessments");
            view_assessments(course);
            System.out.print("Enter ID of assessment to view submissions:");
            int as_index=sc.nextInt();
            System.out.println("Choose ID from these ungraded submissions");
            for(int i=0;i<Student.get_tot_stu();i++){
                if(course.get_assessments().get(as_index).get_status(i).equals("ungraded")){
                    System.out.printf("%d. S%d\n",i,i);
                }
            }
            int stu_index=sc.nextInt();
            course.get_assessments().get(as_index).get_submission(stu_index);
            course.get_assessments().get(as_index).grade(stu_index,prof_name);
            run(bag);
        }
        if(in==6){
            System.out.println("List of Open Assignments:");
            for(int i=0;i<course.get_assessments().size();i++) {
                if(!course.get_assessments().get(i).if_closed()){
                    System.out.print("ID: " + Integer.toString(i)+" ");
                    course.get_assessments().get(i).view();
                    System.out.println("----------------");
                }
            }
            System.out.print("Enter id of assignment to close: ");
            Scanner scan=new Scanner(System.in);
            int close_index=scan.nextInt();
            course.get_assessments().get(close_index).close();
            run(bag);
        }
        if(in==7){
            view_comments();
            run(bag);
        }
        if(in==8){
            add_comments();
            run(bag);
        }
        if(in==9){
            bag.run();
        }
    }
}
class Student implements User{
    private String stu_name;
    private Course course;
    private static int count=0;
    private int stu_id;

    public Student(String name,Course course){
        this.stu_name=name;
        this.course=course;
        this.stu_id=count;
        count++;
        for(Assessments i:course.get_assessments()) i.add_student();
    }
    public static int get_tot_stu(){
        return count;
    }
    public String get_name(){
        return stu_name;
    }
    public void run(Bagpack bag){
        System.out.println("Welcome "+stu_name);
        System.out.println("STUDENT MENU\n" +
                "1. View lecture materials\n" +
                "2. View assessments\n" +
                "3. Submit assessment\n" +
                "4. View grades\n" +
                "5. View comments\n" +
                "6. Add comments\n" +
                "7. Logout");
        Scanner sc=new Scanner(System.in);
        int in=sc.nextInt();
        if(in==1){
            view_lec_material(course);
            run(bag);
        }
        if(in==2){
            view_assessments(course);
            run(bag);
        }
        if(in==3){
            boolean flag=false;
            boolean flag2=false;
            for(int i=0;i<course.get_assessments().size();i++){
                if(course.get_assessments().get(i).get_status(stu_id).equals("open")){
                    if(flag2==false){
                        System.out.println("Pending assessments");
                        flag2=true;
                    }
                    System.out.print("ID: "+Integer.toString(i)+" ");
                    course.get_assessments().get(i).view();
                    flag=true;
                }
            }
            if(flag==true) {
                System.out.print("Enter ID of assessment: ");
                int id = sc.nextInt();
                course.get_assessments().get(id).submit(stu_id);
            }
            else{
                System.out.println("No pending assessments");
            }
            run(bag);
        }
        if(in==4){
            System.out.println("Graded submissions");
            for(Assessments assessment:course.get_assessments()){
                if(assessment.get_status(stu_id).equals("graded")){
                    assessment.view_grade(stu_id);
                    System.out.println("-----------------");
                }
            }
            System.out.println("Ungraded submissions");
            for(Assessments assessment: course.get_assessments()){
                if(assessment.get_status(stu_id).equals("ungraded")){
                    assessment.get_submission(stu_id);
                    System.out.println("-----------------");
                }
            }

            run(bag);
        }
        if(in==5){
            view_comments();
            run(bag);
        }
        if(in==6){
            add_comments();
            run(bag);
        }
        if(in==7){
            this.logout(bag);
        }
    }
}

class Discussion_Form {
    private static ArrayList<User> user_list=new ArrayList<>();
    private static ArrayList<String> comment_list=new ArrayList<>();
    private static ArrayList<Date> time_list=new ArrayList<>();
    public static void add_comments(User user,String comm){
        user_list.add(user);
        comment_list.add(comm);
        Date instant= new Date();
        time_list.add(instant);
    }
    public static void view_comments(){
        for(int i=0;i<comment_list.size();i++){
            System.out.println(comment_list.get(i)+"-"+user_list.get(i).get_name());
            System.out.println(time_list.get(i));
            System.out.println("");
        }
    }
}

interface Lecture_material{
    void view();
}

class Lec_vid implements Lecture_material{
    private String filename,topic,prof_name;
    private Date time;
    public Lec_vid(String topic,String name,String prof_name){
        this.filename=name;
        this.topic=topic;
        this.time=new Date();
        this.prof_name=prof_name;
    }
    public void view(){
        System.out.println("\nTitle of video: "+topic);
        System.out.println("Video file:"+filename);
        System.out.print("Date of upload: ");
        System.out.println(this.time);
        System.out.println("Uploaded by: "+prof_name);
    }
}
class Lec_slides implements Lecture_material{
    private String topic,prof_name;
    private int num_slides;
    private Date time;
    private ArrayList<String> content=new ArrayList<>();
    public Lec_slides(String topic,int num_slides,ArrayList content,String prof_name){
        this.topic=topic;
        this.num_slides=num_slides;
        this.content=content;
        this.time=new Date();
        this.prof_name=prof_name;
    }
    public void view(){
        System.out.println("Title: "+topic);
        for(int i=0;i<num_slides;i++){
            System.out.printf("Slide %d: %S\n",i+1,content.get(i));
        }
        System.out.println("Number of slides:"+Integer.toString(num_slides));
        System.out.print("Date of upload: ");
        System.out.println(this.time);
        System.out.println("Uploaded by: "+prof_name);

    }

}
interface Assessments {
    void view();
    void grade(int x,String s);
    void view_grade(int x);
    void close();
    void submit(int x);
    String get_status(int x);
    void add_student();
    void get_submission(int x);
    boolean if_closed();
}
class Assignments implements Assessments{
    private String prob_stat;
    private int max_marks;
    private ArrayList<String> status=new ArrayList<>();
    private ArrayList<String> submitted_files=new ArrayList<>();
    private ArrayList<String> graded_by_prof=new ArrayList<>();
    private ArrayList<Integer> grades=new ArrayList<>();
    private boolean if_closed=false;

    public Assignments(String s,int marks) {
        this.prob_stat=s;
        this.max_marks=marks;
        for(int i=0;i<Student.get_tot_stu();i++){
            this.status.add("open");
            this.submitted_files.add("");
            this.graded_by_prof.add("");
            this.grades.add(0);
        }
    }
    @Override
    public void add_student(){
        status.add("");
        submitted_files.add("");
        graded_by_prof.add("");
        grades.add(0);
    }
    @Override
    public void get_submission(int stu_id){
        System.out.println("Submission:"+submitted_files.get(stu_id));
    }
    @Override
    public String get_status(int stu_id){
        return status.get(stu_id);
    }

    @Override
    public void view() {
        System.out.printf("Assignment: %S Max Marks: %d\n",prob_stat,max_marks);
    }
    @Override
    public void grade(int stu_id,String prof_name){
        System.out.println("-------------------------------");
        System.out.println("Max Marks: "+Integer.toString(max_marks));
        System.out.print("Marks scored: ");
        Scanner sc=new Scanner(System.in);
        int gr=sc.nextInt();
        this.grades.set(stu_id,gr);
        this.status.set(stu_id,"graded");
        this.graded_by_prof.set(stu_id,prof_name);
    }
    @Override
    public void view_grade(int stu_id){
        get_submission(stu_id);
        System.out.println("Marks scored: "+Integer.toString(grades.get(stu_id)));
        System.out.println("Graded by: "+graded_by_prof.get(stu_id));
    }
    @Override
    public boolean if_closed(){
        return if_closed;
    }
    @Override
    public void close(){
        this.if_closed=true;
        for(int i=0;i<status.size();i++){
            if(!this.status.get(i).equals("graded")) {
                this.status.set(i,"closed");
            }
        }
    }
    @Override
    public void submit(int stu_id){
        System.out.print("Enter filename of assignment: ");
        Scanner sc=new Scanner(System.in);
        String filename=sc.nextLine();
        if(filename.endsWith(".zip")){
            submitted_files.set(stu_id,filename);
            this.status.set(stu_id,"ungraded");
        }
        else{
            System.out.println("invalid input. file must have a .zip extenson!");
        }
    }
}
class Quizzes implements Assessments{
    private String question;
    private int max_marks;
    private ArrayList<String> status=new ArrayList<>();
    private ArrayList<String> submitted_files=new ArrayList<>();
    private ArrayList<String> graded_by_prof=new ArrayList<>();
    private ArrayList<Integer> grades=new ArrayList<>();
    private boolean if_closed=false;

    public Quizzes(String s){
        this.question=s;
        this.max_marks=1;
        for(int i=0;i<Student.get_tot_stu();i++){
            this.status.add("open");
            this.submitted_files.add("");
            this.graded_by_prof.add("");
            this.grades.add(0);
        }
    }
    @Override
    public void add_student(){
        status.add("");
        submitted_files.add("");
        graded_by_prof.add("");
        grades.add(0);
    }
    @Override
    public void get_submission(int stu_id){
        System.out.println("Submission:"+submitted_files.get(stu_id));
    }
    @Override
    public String get_status(int stu_id){
        return status.get(stu_id);
    }
    @Override
    public void view() {
        System.out.printf("Question: %S\n",question);
    }
    @Override
    public void grade(int stu_id,String prof_name){
        System.out.println("-------------------------------");
        System.out.println("Max Marks: "+Integer.toString(max_marks));
        System.out.print("Marks scored: ");
        Scanner sc=new Scanner(System.in);
        int gr=sc.nextInt();
        this.grades.set(stu_id,gr);
        this.status.set(stu_id,"graded");
        this.graded_by_prof.set(stu_id,prof_name);
    }
    @Override
    public void view_grade(int stu_id){
        get_submission(stu_id);
        System.out.println("Marks scored: "+Integer.toString(grades.get(stu_id)));
        System.out.println("Graded by: "+graded_by_prof.get(stu_id));
    }
    @Override
    public boolean if_closed(){
        return if_closed;
    }
    @Override
    public void close(){
        this.if_closed=true;
        for(int i=0;i<status.size();i++){
            if(!this.status.get(i).equals("graded")) {
                this.status.set(i,"closed");
            }
        }
    }
    public void submit(int stu_id){
        System.out.print(question);
        Scanner sc=new Scanner(System.in);
        String filename=sc.nextLine();
        submitted_files.set(stu_id,filename);
    }
}

class Course{
//    private ArrayList<Lec_slides> lec_slide_list=new ArrayList<>();
//    private ArrayList<Lec_vid> lec_vid_list=new ArrayList<>();
    private ArrayList<Lecture_material> lec_mat_list=new ArrayList<>();
    private ArrayList<Assessments> assessments_list=new ArrayList<>();
    private Discussion_Form form;

    public Course(){
        form=new Discussion_Form();
    }
//    public ArrayList<Lec_slides> get_Lec_slides(){return lec_slide_list;}
//    public ArrayList<Lec_vid> get_Lec_vids(){return lec_vid_list;}
    public ArrayList<Lecture_material> get_Lec_mat(){return lec_mat_list;}
    public void add_Lec_mat(Lecture_material lec){lec_mat_list.add(lec);}
    public ArrayList<Assessments> get_assessments(){return assessments_list;}
    public void add_assessments(Assessments assessment){assessments_list.add(assessment);}
//    public void add_Lec_slides(Lec_slides lec){lec_slide_list.add(lec);}
//    public void add_Lec_vids(Lec_vid lec){lec_vid_list.add(lec);}
}

class Bagpack{
    private ArrayList<Professor> prof_list=new ArrayList<>();
    private ArrayList<Student> stu_list=new ArrayList<>();
    private Professor prof;
    private int num_courses,num_instructors,num_students;
    private Course course;

    public Bagpack(int y,int z){
        this.course=new Course();
        this.num_instructors=y;
        this.num_students=z;
        for(int i=0;i<y;i++){
            Professor prof=new Professor("I"+Integer.toString(i),course);
            prof_list.add(prof);
        }
        for(int i=0;i<z;i++){
            Student stu=new Student("S"+Integer.toString(i),course);
            stu_list.add(stu);
        }
        this.run();
    }
    public void run(){
        System.out.println("Welcome to Backpack\n" +
                "1. Enter as instructor\n" +
                "2. Enter as student\n" +
                "3. Exit");
        Scanner sc=new Scanner(System.in);
        int in=sc.nextInt();
        if(in==1){
            System.out.println("Instructors:");
            for(int i=0;i<prof_list.size();i++){
                System.out.printf("%d-I%d\n",i,i);
            }
            System.out.print("Choose id:");
            int prof_id=sc.nextInt();
            prof_list.get(prof_id).run(this);
        }
        else if(in==2){
            System.out.println("Students:");
            for(int i=0;i<stu_list.size();i++){
                System.out.printf("%d-S%d\n",i,i);
            }
            System.out.print("Choose id:");
            int stu_id=sc.nextInt();
            stu_list.get(stu_id).run(this);
        }
    }
}
public class Main2 {

    public static void main(String[] args) {
        Bagpack bag=new Bagpack(2,3);
    }
}
