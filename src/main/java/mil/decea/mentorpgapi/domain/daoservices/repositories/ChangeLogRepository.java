package mil.decea.mentorpgapi.domain.daoservices.repositories;

import mil.decea.mentorpgapi.domain.changewatch.logs.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long>, JpaSpecificationExecutor<ChangeLog> {

    List<ChangeLog> findByParentClassAndParentIdAndObjectClassAndObjectId(String parentClass, Long parentId, String objectClass, Long objectId);
    List<ChangeLog> findByParentClassAndParentId(String parentClass, Long parentId);

}
