package k23cnt3.pdpDay03.k23cnt3pdpDay03.controller;

import k23cnt3.pdpDay03.k23cnt3pdpDay03.entity.Student;
import k23cnt3.pdpDay03.k23cnt3pdpDay03.service.ServiceStudent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private ServiceStudent studentService;

    // GET tất cả sinh viên
    @GetMapping("/student-list")
    public List<Student> getStudents() {
        return studentService.getStudents();
    }

    // GET 1 sinh viên theo id
    @GetMapping("/student/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable("id") Long id) {
        Student student = studentService.getStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    // POST thêm sinh viên
    @PostMapping("/student-add")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    // PUT cập nhật sinh viên
    @PutMapping("/student/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable("id") Long id,
                                                 @RequestBody Student student) {
        Student updated = studentService.updateStudent(id, student);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    // DELETE sinh viên
    @DeleteMapping("/student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        boolean deleted = studentService.deleteStudent(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
