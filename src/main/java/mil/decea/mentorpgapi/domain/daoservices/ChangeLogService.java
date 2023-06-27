package mil.decea.mentorpgapi.domain.daoservices;


import mil.decea.mentorpgapi.domain.changewatch.logs.ChangeLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.ChangeLogRecord;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.logs.RequestLogsRecord;
import mil.decea.mentorpgapi.domain.daoservices.repositories.ChangeLogRepository;
import mil.decea.mentorpgapi.domain.user.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Async
    public void insert(ChangeLog log){
        changeLogRepository.save(log);
    }

    @Async
    public void insert(FieldChangedWatcher log){//, AuthUser authUser
        saveLogsService.insert(log);
    }

    @Async
    public void insert(List<ChangeLog> logs){
        saveLogsService.saveLogs(logs);
    }

    @Async
    public void insert(Collection<FieldChangedWatcher> logs){//, AuthUser authUser
        if (logs != null){
            saveLogsService.saveLogs(logs);
        }
    }



    public Page<ChangeLogRecord> getAllLogsFor(RequestLogsRecord req, Pageable pageable){
        return changeLogRepository.getLogs(req,pageable);
        /*Page<ChangeLog> _page;
        if (req.objectClass() == null){
            _page = changeLogRepository.findByParentClassAndParentId(req.parentClass(), req.parentId(), pageable);
        }else{
            _page = changeLogRepository.findByParentClassAndParentIdAndObjectClassAndObjectId(req.parentClass(), req.parentId(), req.objectClass(), req.objectId(), pageable);
        }
        Page<ChangeLogRecord> p2 = new PageImpl<>(_page.getContent().stream().map(ChangeLogRecord::new).toList(), pageable, _page.getTotalElements());

        return p2;*/
    }


    @Service
    static class SaveLogsService {

        private final ChangeLogRepository changeLogRepository;
        @Autowired
        public SaveLogsService(ChangeLogRepository changeLogRepository) {
            this.changeLogRepository = changeLogRepository;
        }


        public void insert(FieldChangedWatcher log){
            AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String nome = authUser.getNome() + " (" + authUser.getCpf() + ")";
            changeLogRepository.save(new ChangeLog(log, authUser.getId(), nome));
        }
        @Transactional
        public void saveLogs(List<ChangeLog> logs){
            changeLogRepository.saveAll(logs);
        }

        @Transactional
        public void saveLogs(Collection<FieldChangedWatcher> logs){//, AuthUser authUser
            AuthUser authUser = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String nome = authUser.getNome() + " (" + authUser.getCpf() + ")";
            saveLogs(logs.stream().map(w -> new ChangeLog(w, authUser.getId(), nome)).toList());
        }
    }

}

