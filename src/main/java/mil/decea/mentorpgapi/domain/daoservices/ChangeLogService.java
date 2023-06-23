package mil.decea.mentorpgapi.domain.daoservices;


import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.logs.ChangeLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.daoservices.repositories.ChangeLogRepository;
import mil.decea.mentorpgapi.domain.user.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class ChangeLogService {

    private final ChangeLogRepository changeLogRepository;
    private final SaveLogsService saveLogsService;

    @Autowired
    public ChangeLogService(ChangeLogRepository changeLogRepository, SaveLogsService saveLogsService) {
        this.changeLogRepository = changeLogRepository;
        this.saveLogsService = saveLogsService;
    }

    public void saveLog(ChangeLog log){
        changeLogRepository.save(log);
    }

    public void saveLog(FieldChangedWatcher log, AuthUser authUser){
        String nome = authUser.getCpf() + " " + authUser.getNome();
        saveLog(new ChangeLog(log, authUser.getId(), nome));
    }

    @Async
    public void insertLogs(List<ChangeLog> logs){
        saveLogsService.saveLogs(logs);
    }

    @Async
    public void insertLogs(Collection<FieldChangedWatcher> logs, AuthUser authUser){
        if (logs != null){
            saveLogsService.saveLogs(logs,authUser);
        }
    }

    public List<ChangeLog> getAllLogsFor(TrackedEntity entity){
        return changeLogRepository.findByParentClassAndParentId(entity.getClass().getName(), entity.getId());
    }

    public List<ChangeLog> getAllLogsFor(TrackedEntity parentEntity, TrackedEntity entity){
        return changeLogRepository.findByParentClassAndParentIdAndObjectClassAndObjectId(
                parentEntity.getClass().getName(),
                parentEntity.getId(),
                entity.getClass().getName(),
                entity.getId()
        );
    }

    @Service
    static class SaveLogsService {

        private final ChangeLogRepository changeLogRepository;
        @Autowired
        public SaveLogsService(ChangeLogRepository changeLogRepository) {
            this.changeLogRepository = changeLogRepository;
        }

        @Transactional
        public void saveLogs(List<ChangeLog> logs){
            changeLogRepository.saveAll(logs);
        }

        @Transactional
        public void saveLogs(Collection<FieldChangedWatcher> logs, AuthUser ausr){
            String nome = ausr.getCpf() + " " + ausr.getNome();
            saveLogs(logs.stream().map(w -> new ChangeLog(w, ausr.getId(), nome)).toList());
        }
    }

}

