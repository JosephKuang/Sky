package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对明文密码进行加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     * &#064;Override可以省略
     */


    public void save(EmployeeDTO employeeDTO) {
        System.out.println("当前线程id"+ Thread.currentThread().getId());

        Employee employee = new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        //设置账号状态，1表示正常，0锁定
        employee.setStatus(StatusConstant.ENABLE);


        //设置默认密码
        //TODO 更换加密方式
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //设置创建和更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        //设置当前记录创建人id和最后修改人id(即当前登陆用户的id)
//        Long empId = BaseContext.getCurrentId();
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);
//        BaseContext.removeCurrentId();

        employeeMapper.insert(employee);



    }

    @Override
    public PageResult selectByPage(EmployeePageQueryDTO employeePageQueryDTO) {
//        int pageNum = Integer.parseInt(String.valueOf(employeePageQueryDTO.getPage()));
//        int size = Integer.parseInt(String.valueOf(employeePageQueryDTO.getPageSize()));
//        int offset = (pageNum - 1) * size;
//        int total = employeeMapper.countAll();
//        List<Employee> records= employeeMapper.selectByPage(offset,size);
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.selectByPage(employeePageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .build();


        employeeMapper.update( employee);
    }

    @Override
    public Employee selectById(Long id) {
      Employee employee =   employeeMapper.selectById(id);
      employee.setPassword("****");
        return employee;
    }


    @Override
    public void updateEmpInfo(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.update(employee);
    }


}
