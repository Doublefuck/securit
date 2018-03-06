CREATE TABLE sys_dept(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '部门id',
  NAME VARCHAR(20) NOT NULL DEFAULT '' COMMENT '部门名称',
  parent_id INT NOT NULL DEFAULT 0 COMMENT '上级部门id',
  LEVEL VARCHAR(200) NOT NULL DEFAULT '' COMMENT '部门层级',
  seq INT NOT NULL DEFAULT 0 COMMENT '部门在当前层级下的顺序，由小到大',
  remark VARCHAR(200) DEFAULT '' COMMENT '备注',
  operator VARCHAR(20) NOT NULL COMMENT '操作人员',
  operator_time DATETIME DEFAULT NOW() COMMENT '最后一次操作时间',
  operator_ip VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次更新操作者的IP'
) COMMENT='部门表';

CREATE TABLE sys_user(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '用户id',
  username VARCHAR(20) NOT NULL DEFAULT '' COMMENT '用户名',
  telephone VARCHAR(13) NOT NULL DEFAULT '' COMMENT '手机号码',
  email VARCHAR(20) NOT NULL DEFAULT '' COMMENT '邮箱',
  PASSWORD VARCHAR(40) NOT NULL COMMENT '加密后的密码',
  dept_id INT NOT NULL DEFAULT 0 COMMENT '用户所在部门的id',
  STATUS INT NOT NULL DEFAULT 1 COMMENT '用户状态，1代表正常，0代表冻结状态，2代表已被删除',
  remark VARCHAR(200) NOT NULL DEFAULT '' COMMENT '备注',
  operator VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次的操作人员',
  operator_time DATETIME DEFAULT NOW() COMMENT '最后一次操作时间',
  operator_ip VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次更新操作者的IP'
) COMMENT='用户表';

CREATE TABLE sys_acl_module(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '权限模块id',
  NAME VARCHAR(20) NOT NULL DEFAULT '' COMMENT '权限模块名称',
  parent_id INT NOT NULL DEFAULT 0 COMMENT '上级权限id',
  LEVEL VARCHAR(200) NOT NULL DEFAULT '' COMMENT '权限模块层级',
  seq INT NOT NULL DEFAULT 0 COMMENT '权限模块在当前层级下的顺序，由小到大',
  STATUS INT NOT NULL DEFAULT 1 COMMENT '用户状态，1代表正常，0代表冻结状态',
  remark VARCHAR(200) DEFAULT '' COMMENT '备注',
  operator VARCHAR(20) NOT NULL COMMENT '操作人员',
  operator_time DATETIME DEFAULT NOW() COMMENT '最后一次操作时间',
  operator_ip VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者的IP'
)comment='权限模块表';

CREATE TABLE sys_acl(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '权限id',
  code varchar(20) not null default '' comment '权限码',
  NAME VARCHAR(20) NOT NULL DEFAULT '' COMMENT '权限名称',
  acl_module_id int not null default 0 comment '权限所属的权限模块id',
  url varchar(100) not null default '' comment '请求的url，可以填写正则',
  type int not null default 1 comment '权限类型，1：菜单，2：按钮，3：其他',
  status int not null default 1 comment '权限状态，1：正常，0：冻结',
  seq int not null default 0 comment '权限在当前模块下的顺序，由小到大',
  remark VARCHAR(200) DEFAULT '' COMMENT '备注',
  operator VARCHAR(20) NOT NULL COMMENT '操作人员',
  operator_time DATETIME DEFAULT NOW() COMMENT '最后一次操作时间',
  operator_ip VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者的IP'
)COMMENT='权限表';

CREATE TABLE sys_role(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '角色id',
  name varchar(20) not null comment '角色名称',
  type int not null default 1 comment '角色类型,1管理员，2其他',
  STATUS INT NOT NULL DEFAULT 1 COMMENT '权限状态，1：正常，0：冻结',
  remark VARCHAR(200) DEFAULT '' COMMENT '备注',
  operator VARCHAR(20) NOT NULL COMMENT '操作人员',
  operator_time DATETIME DEFAULT NOW() COMMENT '最后一次操作时间',
  operator_ip VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者的IP'
)COMMENT='角色表';

CREATE TABLE sys_role_user(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '',
  role_id int not null comment '角色id',
  user_id int not null comment '用户id',
  operator VARCHAR(20) NOT NULL default '' COMMENT '操作人员',
  operator_time DATETIME DEFAULT NOW() COMMENT '最后一次操作时间',
  operator_ip VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者的IP'
)COMMENT='角色用户关联表';

CREATE TABLE sys_role_acl(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '',
  role_id INT NOT NULL COMMENT '角色id',
  acl_id INT NOT NULL COMMENT '权限id',
  operator VARCHAR(20) NOT NULL DEFAULT '' COMMENT '操作人员',
  operator_time DATETIME DEFAULT NOW() COMMENT '最后一次操作时间',
  operator_ip VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者的IP'
)COMMENT='角色权限关联表';

CREATE TABLE sys_log(
  id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  type int not null default 0 comment '权限更新类型，1部门，2用户，3权限模块，4权限，5角色，6角色用户关系，7角色权限关系',
  target_id int not null comment '基于type结果指定的对象id，比如用户权限、权限、角色等表的主键',
  old_value text comment '旧值',
  new_value text comment '新值',
  operator VARCHAR(20) NOT NULL DEFAULT '' COMMENT '操作人员',
  operator_time DATETIME DEFAULT NOW() COMMENT '最后一次操作时间',
  operator_ip VARCHAR(20) NOT NULL DEFAULT '' COMMENT '最后一次操作者的IP',
  status int not null comment '当前是否进行过复原操作，0没有，1复原过'
)COMMENT='权限相关更新记录表';

/*  177 5413 8672
	每个表都有自己的主键
	字段尽量定义为not null
	尽量为每一个字段添加备注
	数据库字典统一小写，单词之间使用下划线
	使用innoDB存储引擎
	尽量使用varchar
*/

insert into sys_dept values(3,'前端开发',1,'0.1',2,'','system',now(),'localhost');
select * from sys_user where dept_id = 1 order by username limit 0,10;
select * from sys_acl_module;
select count(1) from sys_user where dept_id = 1;
delete from sys_acl_module where id in (4,5);
select * from sys_role;