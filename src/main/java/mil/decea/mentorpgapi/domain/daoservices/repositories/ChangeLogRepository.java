package mil.decea.mentorpgapi.domain.daoservices.repositories;

import mil.decea.mentorpgapi.domain.changewatch.logs.ChangeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, JpaSpecificationExecutor<ChangeLog> {

    List<ChangeLog> findByParentClassAndParentIdAndObjectClassAndObjectId(String parentClass, Long parentId, String objectClass, Long objectId);
    Page<ChangeLog> findByParentClassAndParentIdAndObjectClassAndObjectId(String parentClass, Long parentId, String objectClass, Long objectId, Pageable pageable);
    List<ChangeLog> findByParentClassAndParentId(String parentClass, Long parentId);
    Page<ChangeLog> findByParentClassAndParentId(String parentClass, Long parentId, Pageable pageable);

}
