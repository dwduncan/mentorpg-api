package mil.decea.mentorpgapi.domain.daoservices.repositories;

import mil.decea.mentorpgapi.domain.changewatch.logs.ChangeLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.ChangeLogRecord;
import mil.decea.mentorpgapi.domain.changewatch.logs.RequestLogsRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, JpaSpecificationExecutor<ChangeLog> {

    List<ChangeLog> findByParentClassAndParentIdAndObjectClassAndObjectId(String parentClass, Long parentId, String objectClass, Long objectId, Sort sort);
    Page<ChangeLog> findByParentClassAndParentIdAndObjectClassAndObjectId(String parentClass, Long parentId, String objectClass, Long objectId, Pageable pageable);
    List<ChangeLog> findByParentClassAndParentId(String parentClass, Long parentId, Sort sort);
    Page<ChangeLog> findByParentClassAndParentId(String parentClass, Long parentId, Pageable pageable);

    default Page<ChangeLogRecord> getLogs(RequestLogsRecord req, Pageable pageable){
        Page<ChangeLog> _page;

        String parentClass = req.parentClass(),  objectClass = req.objectClass();
        Long parentId = req.parentId(),  objectId = req.objectId();

        if (parentClass == null && objectClass != null){
            parentClass = objectClass;
            objectClass = null;
        }

        if (objectClass == null){
            _page = findByParentClassAndParentId(parentClass, parentId, pageable);
        }else{
            _page = findByParentClassAndParentIdAndObjectClassAndObjectId(parentClass, parentId, objectClass, objectId, pageable);
        }

        return new PageImpl<>(_page.getContent().stream().map(ChangeLogRecord::new).toList(), pageable, _page.getTotalElements());
    }

}
