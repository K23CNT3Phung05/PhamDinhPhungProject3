package k23cnt3.pdpDay03.k23cnt3pdpDay03.service;

import k23cnt3.pdpDay03.k23cnt3pdpDay03.entity.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ServiceStudent {

    private List<Student> students = new ArrayList<>();

    // Khởi tạo danh sách sinh viên mẫu
    public ServiceStudent() {
        students.addAll(Arrays.asList(
                new Student(1L,"Devmaster 1",20,"Non","Số 25VNP","0978611889","chungtrinhj@gmail.com"),
                new Student(2L,"Devmaster 2",25,"Non","Số 25VNP","0978611889","contact@devmaster.edu.vn"),
                new Student(3L,"Devmaster 3",22,"Non","Số 25VNP","0978611889","chungtrinhj@gmail.com")
        ));
    }

    // Lấy tất cả sinh viên
    public List<Student> getStudents() {
        return students;
    }

    // Lấy sinh viên theo id
    public Student getStudent(Long id) {
        return students.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst().orElse(null);
    }

    // Thêm sinh viên mới
    public Student addStudent(Student student) {
        // Tự sinh id mới
        long maxId = students.stream()
                .mapToLong(Student::getId)
                .max()
                .orElse(0L);
        student.setId(maxId + 1);
        students.add(student);
        return student;
    }

    // Cập nhật thông tin sinh viên
    public Student updateStudent(Long id, Student student) {
        Student check = getStudent(id);
        if (check == null) {
            return null;
        }
        check.setName(student.getName());
        check.setAddress(student.getAddress());
        check.setEmail(student.getEmail());
        check.setPhone(student.getPhone());
        check.setAge(student.getAge());
        check.setGender(student.getGender());
        return check;
    }

    // Xóa sinh viên
    public boolean deleteStudent(Long id) {
        Student check = getStudent(id);
        return students.remove(check);
    }
}
