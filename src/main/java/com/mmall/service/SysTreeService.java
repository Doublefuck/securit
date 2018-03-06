package com.mmall.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.mmall.dao.SysAclMapper;
import com.mmall.dao.SysAclModuleMapper;
import com.mmall.dao.SysDeptMapper;
import com.mmall.dto.AclDto;
import com.mmall.dto.AclModuleLevelDto;
import com.mmall.dto.DeptLevelDto;
import com.mmall.model.SysAcl;
import com.mmall.model.SysAclModule;
import com.mmall.model.SysDept;
import com.mmall.service.inter.ISysTreeService;
import com.mmall.util.LevelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Administrator on 2018/2/27 0027.
 */
@Slf4j
@Service
public class SysTreeService implements ISysTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysCoreService sysCoreService;

    @Resource
    private SysAclMapper sysAclMapper;

    // user<--role-->acl
    /**
     * 角色对应的权限树
     * @param roleId
     * @return
     */
    public List<AclModuleLevelDto> roleTree(Integer roleId) {

        // 当前用户被分配过的权限点列表
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
        // 根据当前用户权限列表获取当前用户的权限id集合
        Set<Integer> userAclIdSet = Sets.newHashSet();
        for (SysAcl sysAcl : userAclList) {
            Integer userAclId = sysAcl.getId();
            userAclIdSet.add(userAclId);
        }

        // 根据roleId获取当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        // 根据当前角色的权限集合获取当前角色的权限id集合
        Set<Integer> roleAclIdSet = Sets.newHashSet();
        for (SysAcl sysAcl : roleAclList) {
            Integer roleAclId = sysAcl.getId();
            roleAclIdSet.add(roleAclId);
        }

        // 取roleAclList和userAclList的并集
        // Set<SysAcl> aclSet = new HashSet<SysAcl>(roleAclList);
        // aclSet.addAll(userAclList);

        // 获取所有的权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();

        // 遍历系统的所有权限，判断用户可使用的权限和角色所具有的权限
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl sysAcl : allAclList) {
            AclDto aclDto = AclDto.adapt(sysAcl); // 根据SysAcl适配AclDto对象
            if (userAclIdSet.contains(sysAcl.getId())) {
                aclDto.setHasAcl(true); // 权限可使用
            }
            if (roleAclIdSet.contains(sysAcl.getId())) {
                aclDto.setChecked(true); // 权限默认被选中
            }
            aclDtoList.add(aclDto);
        }
        return aclListToTree(aclDtoList);
    }

    /**
     * 获取可用的权限，并绑定到权限模块上
     * @param aclDtoList
     * @return
     */
    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        // 获取系统的整个权限树
        List<AclModuleLevelDto> aclModuleLevelDtoList = aclModuleTree();
        // 每一个权限模块下面的所有权限点，key是权限模块id，value是权限点集合
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        // AclDto继承自SysAcl
        for (AclDto aclDto : aclDtoList) {
            if (aclDto.getStatus() == 1) {
                // 权限可用
                moduleIdAclMap.put(aclDto.getAclModuleId(), aclDto);
            }
        }
        bindAclsWithOrder(aclModuleLevelDtoList, moduleIdAclMap);
        return aclModuleLevelDtoList;
    }

    /**
     * 遍历权限模块层级，获取当前层级权限集合，并将可用权限集合绑定到当前权限模块上
     * @param aclModuleLevelDtoList
     * @param moduleIdAclMap
     */
    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelDtoList, Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelDtoList)) {
            return;
        }
        // 遍历当前权限模块层级，获取当前层级的权限
        for (AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList) {
            // Multimap的get(key)方法返回value对应的集合Collection
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(aclModuleLevelDto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                // 排序
                Collections.sort(aclDtoList, new Comparator<AclDto>() {
                    public int compare(AclDto o1, AclDto o2) {
                        return o1.getSeq() - o2.getSeq();
                    }
                });
                // 将当前的权限点绑定在权限模块上
                aclModuleLevelDto.setAclDtoList(aclDtoList);
            }
            bindAclsWithOrder(aclModuleLevelDto.getAclModuleLevelDtoList(), moduleIdAclMap);
        }
    }

    /**
     * 获取当前所有权限模块并组装成树状结构
     * @return
     */
    public List<AclModuleLevelDto> aclModuleTree(){
        // 获取当前所有的权限模块
        List<SysAclModule> sysAclModuleList = sysAclModuleMapper.getAllAclModule();
        log.info(String.valueOf(sysAclModuleList.size()));

        List<AclModuleLevelDto> aclModuleLevelDtoList = Lists.newArrayList();
        for(SysAclModule sysAclModule : sysAclModuleList){
            aclModuleLevelDtoList.add(AclModuleLevelDto.adapt(sysAclModule));
        }
        return aclModuleLevelDtoListTree(aclModuleLevelDtoList);
    }

    /**
     * 组装权限模块树状结构（递归）
     * @param aclModuleLevelDtoList
     * @return
     */
    public List<AclModuleLevelDto> aclModuleLevelDtoListTree(List<AclModuleLevelDto> aclModuleLevelDtoList){

        if(CollectionUtils.isEmpty(aclModuleLevelDtoList)){
            return Lists.newArrayList();
        }

        // level -> {aclMdule1,aclMdule2,aclMdule3,...}
        //将层级作为key，将该层级下所有权限模块集合作为value，使用高级Map类组装树形结构
        Multimap<String,AclModuleLevelDto> aclModuleLevelDtoMultimap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for(AclModuleLevelDto aclModuleLevelDto : aclModuleLevelDtoList){
            aclModuleLevelDtoMultimap.put(aclModuleLevelDto.getLevel(),aclModuleLevelDto);
            if(LevelUtil.ROOT.equals(aclModuleLevelDto.getLevel())){
                rootList.add(aclModuleLevelDto);
            }
        }

        // 排序首层模块rootList
        Collections.sort(rootList, new Comparator<AclModuleLevelDto>() {
            public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        // 一首层模块为基础，每一层级按照seq递归排序
        transformAclModuleTree(rootList, LevelUtil.ROOT,aclModuleLevelDtoMultimap);
        log.info(String.valueOf(rootList.size()) + "------");
        return rootList;
    }

    /**
     * 根据seq递归排序每一层的权限模块，停止条件是权限模块集合为空
     * @param aclModuleLevelDtoList
     * @param level
     * @param aclModuleLevelDtoMultimap
     */
    public void transformAclModuleTree(List<AclModuleLevelDto> aclModuleLevelDtoList, String level, Multimap<String,AclModuleLevelDto> aclModuleLevelDtoMultimap) {
        for(int i = 0;i < aclModuleLevelDtoList.size();i++){
            // 获取当前层级的权限模块
            AclModuleLevelDto aclModuleLevelDto = aclModuleLevelDtoList.get(i);
            // 处理当前层级，组装成下一层级
            String nextLevel = LevelUtil.calculateLevel(level,aclModuleLevelDto.getId());
            // 根据组装的下一层及获取下一层级的所有权限模块
            List<AclModuleLevelDto> aclModuleLevelDtos = (List<AclModuleLevelDto>) aclModuleLevelDtoMultimap.get(nextLevel);

            if(CollectionUtils.isNotEmpty(aclModuleLevelDtos)){
                Collections.sort(aclModuleLevelDtoList, new Comparator<AclModuleLevelDto>() {
                    public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
                        return o1.getSeq() - o2.getSeq();
                    }
                });
                aclModuleLevelDto.setAclModuleLevelDtoList(aclModuleLevelDtos);
                transformAclModuleTree(aclModuleLevelDtos, nextLevel, aclModuleLevelDtoMultimap);
            }

        }
    }

    /**
     * 获取所有部门并组装成树状结构
     * @return
     */
    public List<DeptLevelDto> deptTree(){
        //获取当前用户的所有部门
        List<SysDept> sysDeptList = sysDeptMapper.getAllDept();

        List<DeptLevelDto> deptLevelDtoList = Lists.newArrayList();
        for(SysDept sysDept : sysDeptList){
            DeptLevelDto deptLevelDto = DeptLevelDto.adapt(sysDept);
            deptLevelDtoList.add(deptLevelDto);
        }
        return deptLevelDtoListToTree(deptLevelDtoList);
    }

    /**
     * 组装部门树状结构
     * @param deptLevelDtoList
     * @return
     */
    public List<DeptLevelDto> deptLevelDtoListToTree(List<DeptLevelDto> deptLevelDtoList){
        if(CollectionUtils.isEmpty(deptLevelDtoList)){
            return Lists.newArrayList();
        }

        // level -> {dept1,dept2,dept3,...}
        //将层级作为key，将该层级下所有部门集合作为value，使用高级Map类组装树形结构
        Multimap<String,DeptLevelDto> deptLevelDtoMultimap = ArrayListMultimap.create();

        List<DeptLevelDto> rootList = Lists.newArrayList();

        for(DeptLevelDto deptLevelDto : deptLevelDtoList){
            deptLevelDtoMultimap.put(deptLevelDto.getLevel(),deptLevelDto);
            if(LevelUtil.ROOT.equals(deptLevelDto.getLevel())){
                rootList.add(deptLevelDto);
            }
        }

        //将rootList按照seq从小到大顺序进行排序
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        //递归生成树状结构
        transformDeptTree(deptLevelDtoList,LevelUtil.ROOT,deptLevelDtoMultimap);

        return rootList;
    }

    /**
     * 递归排序
     * 停止条件是部门集合为空
     * @param deptLevelDtoList
     * @param level
     * @param deptLevelDtoMultimap
     */
    public void transformDeptTree(List<DeptLevelDto> deptLevelDtoList,String level,Multimap<String,DeptLevelDto> deptLevelDtoMultimap){
        for(int i = 0;i < deptLevelDtoList.size();i++){
            //遍历该层的每一个元素
            DeptLevelDto deptLevelDto = deptLevelDtoList.get(i);
            //处理当前的层级
            String nextLevel = LevelUtil.calculateLevel(level,deptLevelDto.getId());
            //处理下一层级
            List<DeptLevelDto> tempDeptLevelDtoList = (List<DeptLevelDto>) deptLevelDtoMultimap.get(nextLevel);
            if(CollectionUtils.isNotEmpty(tempDeptLevelDtoList)){ //递归停止条件
                //按照seq排序
                Collections.sort(tempDeptLevelDtoList, new Comparator<DeptLevelDto>() {
                    public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                        return o1.getSeq() - o2.getSeq();
                    }
                });
                //设置下一层部门
                deptLevelDto.setDeptLevelDtoList(tempDeptLevelDtoList);
                //进入到下一层处理
                transformDeptTree(tempDeptLevelDtoList,nextLevel,deptLevelDtoMultimap);
            }
        }
    }
}
