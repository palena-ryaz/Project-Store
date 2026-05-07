package market;

import java.util.HashMap;
import java.util.Map;

public class Store {
    private Map<ProductType, Department> departments;
    private int time;

    public Store(){
        this.departments = new HashMap<>();
        this.time = 0;
    }

    public void addDepartment(Department department, ProductType type){
        departments.put(type, department);
    }

    public Department getDepartmentByType(ProductType type){
        return departments.get(type);
    }
    // выполнение шагов для всех отделов счётчик шагов
    public void doStep(){
        this.time++;
        System.out.println("\nШАГ " + time);

        for (Department department : departments.values()){
            department.updateStep();
        }
    }
    // добавить покупателя с наполненной корзиной
    public void addCustomer(Customer customer){
        Department department = departments.get(customer.getDesiredType());
        department.fillBasket(customer);
        department.acceptCustomer(customer);

    }
    // найти нужный товар в отделе
    public Product findProduct(String name){
        for (Department department : departments.values()){
            if(department.getProducts().containsKey(name)){
                return department.getProducts().get(name);
            }
        }
        return null;
    }
    // счётчик недождавшихся покупателей
    public int getAllNotWaiting(){
        int count = 0;

        for (Department department : departments.values()){
            count += department.getNotWaiting();
        }
        return count;
    }

    public Map<ProductType, Department> getDepartments(){
        return departments;
    }

    public int getTime() {
        return time;
    }
}
