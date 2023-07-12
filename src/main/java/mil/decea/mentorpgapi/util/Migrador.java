package mil.decea.mentorpgapi.util;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import mil.decea.mentorpgapi.domain.documents.DocumentType;
import mil.decea.mentorpgapi.domain.user.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Migrador {

    static final String projectSrcDir = "./src/main/java/mil/decea/mentorpgapi/domain/";
    static final String atdTargetDir = "/Users/duncandwdi.DECEA/IdeaProjects/MentorPG/src/main/java/fab/mentorpg/migration/";
    static final String homeTargetDir = "/OneDrive/001_ProjetosPessoais/004_Cursos/PrototipoMentorPG3Next/src/api/model";
    static final String atdSourceDir = "/Users/duncandwdi.DECEA/IdeaProjects/MentorAPI/./src/main/java/";//"/C:/Users/duncandwdi.DECEA/IdeaProjects/MentorAPI/./src/main/java/";


    public static void main(String... args){
        String d = """
                    esfserfewsrew
                      public User(Long id,
                                  boolean ativo,
                                  @NotNull(message = "Informe um CPF válido") String cpf,
                                  Titulacao titulacao,
                                  Posto posto,
                                  String quadro,
                                  String especialidade,
                                  @NotNull(message = "Informe o nome de guerra") String nomeGuerra,
                                  @NotNull(message = "Informe o nome completo") String nomeCompleto,
                                  Sexo sexo,
                                  boolean pttc) {
                  
                          super(id, ativo, LocalDateTime.now());
                          this.cpf = cpf;
                          this.titulacao = titulacao;
                          this.posto = posto;
                          this.quadro = quadro;
                          this.especialidade = especialidade;
                          this.nomeGuerra = nomeGuerra;
                          this.nomeCompleto = nomeCompleto;
                          this.sexo = sexo;
                          this.pttc = pttc;
                      }      
                      
                      werwer            
                """;

        /*
        Map<String,String> mapa = replacesInFile(DocumentType.class);
        for(String rep : mapa.keySet()){
            d = d.replaceAll(rep, mapa.get(rep));
        }
        */

        d = d.replaceAll("@NotNull\\(.*\\)\r*\n*\s*","");
        String r = "public " + User.class.getSimpleName() + "\s*\\((.)*\\)\s*\\{[^{}]*}";
        System.out.println(r);
        System.out.println(d.replaceAll(r,""));
        //classesToMigrate(ClassLoader.getSystemClassLoader(), new File(projectSrcDir));
        //generateNewPersistenceXml(atdTargetDir);
    }

    static Set<Class<?>> classesMigradas = new HashSet<>();

    static void classesToMigrate(ClassLoader classLoader, File srcDir){
        File baseDir = new File(atdSourceDir);
        for(File file : srcDir.listFiles()){

            if (file.isDirectory()) {
                classesToMigrate(classLoader, file);
            }else if (file.getName().endsWith(".java")) {
                String className = file.toURI().getPath().replace(baseDir.toURI().getPath(),"").replaceAll("/",".").replace(".java","");
                try {
                    Class<?> c = Class.forName(className, true, classLoader);

                    if (c.isAnnotationPresent(Entity.class) || c.isAnnotationPresent(Embeddable.class)){

                        if (classesMigradas.add(c)) {
                            try {
                                migrateSuperClasses(c);
                                migrateClass(c);
                            }catch (Exception e){
                                e.printStackTrace();
                                break;
                            }
                        }
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static void migrateSuperClasses(Class<?> _class) throws IOException {

        Class<?> sup = _class.getSuperclass();

        if (sup != null && sup != Object.class){

            if (classesMigradas.add(sup)){
                migrateClass(sup);
                migrateSuperClasses(sup);
            }

        }
    }

    static void migrateClass(Class<?> _class) throws IOException {

        String src = atdSourceDir + _class.getName().replace(".","/") + ".java";

        try {
            Path filePath = Path.of(src);
            String content = Files.readString(filePath);
            Map<String,String> mapa = replacesInFile(_class);
            for(String rep : mapa.keySet()){
                content = content.replaceAll(rep, mapa.get(rep));
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(atdTargetDir + _class.getSimpleName() + ".java"));
            writer.write("package fab.mentorpg.migration;\n");
            writer.write(getImports(_class));
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw e;
        }
    }

    static String getImports(Class _class){

        if (User.class.isAssignableFrom(_class)){
            return "import fab.mentorpg.core.pessoal.*;\nimport fab.mentorpg.core.academico.*;\n";
        }

        return "";
    }

    static Map<String,String> replacesInFile(Class<?> _class){

        String entityAnnotation = _class.isAnnotationPresent(Entity.class) ? "@Entity(name = \"" + _class.getSimpleName() + "_NG\")" : "";
        Map<String,String> mapa = new HashMap<>();
        mapa.put("package\s+" + _class.getPackageName() + "\s*;","");
        mapa.put("@Entity",entityAnnotation);
        mapa.put("public String getEntityDescriptor\\(\\)\s*\\{[^{}]*}","");
        mapa.put("public TrackedEntity getParentObject\\(\\)\s*\\{[^{}]*}","");
        mapa.put("public Collection<\\? extends GrantedAuthority> getAuthorities\\(\\)\s*\\{[^{}]*}","");
        mapa.put("public .* updateDocumentsCollections\\(.+\\)\s*\\{[^{}]*}","");
        mapa.put("public void onValuesUpdated\\(.+\\)\s*\\{[^{}]*}","");
        mapa.put("@RecordFieldName(.*)\r*\n*\s*","");
        mapa.put("@NotNull\\(.*\\)\r*\n*\s*","");
        mapa.put("@PreviousValueMessage(.*)\r*\n*\s*","");
        mapa.put("@IsValidCpf\r*\n*\s*","");
        mapa.put("@IgnoreTrackChange\r*\n*\s*","");
        mapa.put("@NotForRecordField\r*\n*\s*","");
        mapa.put("@NotAutomatedTrack\r*\n*\s*","");
        mapa.put("@CollectionForRecordField\\(.*\\)\r*\n*\s*","");
        mapa.put("@Override\r*\n*\s*","");
        mapa.put("import mil\\.decea\\.mentorpgapi.*;\r*\n*\s*","");
        mapa.put("import org\\.springframework\\.security.*;\r*\n*\s*","");
        mapa.put("UserDetails, MinioStorage, ","");
        mapa.put(" implements TrackedEntity","");
        mapa.put("public\s+" + _class.getSimpleName() + "\\(.*\\)\s*\\{[^{}]*}","");
        return mapa;
    }

    static void generateNewPersistenceXml(String targetDir){

        String part1 = """
                <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
                <persistence xmlns="https://jakarta.ee/xml/ns/persistence"
                                 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                                 xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
                                 version="3.0">
                    <persistence-unit name="mentorpg_PU" transaction-type="JTA">
                        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
                        <jta-data-source>jdbc/mentorpgDS</jta-data-source>
                                
                                
                                
                        <class>fab.mentorpg.core.AbstractImagem</class>
                        <class>fab.mentorpg.core.Entidade</class>
                        <class>fab.mentorpg.core.EntidadeLongID</class>
                        <class>fab.mentorpg.core.EntidadeLongIDOrdenavel</class>
                        <class>fab.mentorpg.core.IdFromDB</class>
                        <class>fab.mentorpg.core.OrdenavelIdNaoSequenciado</class>
                                
                        <class>fab.mentorpg.core.academico.AbstractPublicacao</class>
                        <class>fab.mentorpg.core.academico.AreaConcentracao</class>
                        <class>fab.mentorpg.core.academico.AutoPublicacaoPK</class>
                        <class>fab.mentorpg.core.academico.AreaPesquisa</class>
                        <class>fab.mentorpg.core.academico.Autores</class>
                        <class>fab.mentorpg.core.academico.ComprovanteMatricula</class>
                        <class>fab.mentorpg.core.academico.ConsultaOrientador</class>
                        <class>fab.mentorpg.core.academico.CriterioAdmissao</class>
                        <class>fab.mentorpg.core.academico.DespachoConsultaOrientacao</class>
                        <class>fab.mentorpg.core.academico.Disciplina</class>
                        <class>fab.mentorpg.core.academico.DisciplinaCursada</class>
                        <class>fab.mentorpg.core.academico.ExigenciaAdmissao</class>
                        <class>fab.mentorpg.core.academico.ImagemAreaPesquisa</class>
                        <class>fab.mentorpg.core.academico.LinhaDePesquisa</class>
                        <class>fab.mentorpg.core.academico.Pesquisa</class>
                        <class>fab.mentorpg.core.academico.PesquisaAluno</class>
                        <class>fab.mentorpg.core.academico.ProgramaPGITA</class>
                        <class>fab.mentorpg.core.academico.PropostaPremilinar</class>
                        <class>fab.mentorpg.core.academico.Publicacao</class>
                        <class>fab.mentorpg.core.academico.TeseDissertacao</class>
                        <class>fab.mentorpg.core.academico.TurmaIngresso</class>
                        <class>fab.mentorpg.core.academico.relatorio.Bibliografia</class>
                        <class>fab.mentorpg.core.academico.relatorio.CadastroExigencias</class>
                        <class>fab.mentorpg.core.academico.relatorio.CumprimentoExigencia</class>
                        <class>fab.mentorpg.core.academico.relatorio.OrientacaoAluno</class>
                        <class>fab.mentorpg.core.academico.relatorio.RelatorioAcademico</class>
                                
                        <class>fab.mentorpg.core.administracao.AbstractAusente</class>
                        <class>fab.mentorpg.core.administracao.AbstractControleAcesso</class>
                        <class>fab.mentorpg.core.administracao.AbstractDespacho</class>
                        <class>fab.mentorpg.core.administracao.AnexoReuniaoSemanal</class>
                        <class>fab.mentorpg.core.administracao.AtaReuniaoSemanal</class>
                        <class>fab.mentorpg.core.administracao.AusenteReuniaoSemanal</class>
                        <class>fab.mentorpg.core.administracao.CargoFuncao</class>
                        <class>fab.mentorpg.core.administracao.ContatoInstitucional</class>
                        <class>fab.mentorpg.core.administracao.ControleAcessoCandidatos</class>
                        <class>fab.mentorpg.core.administracao.Documento</class>
                        <class>fab.mentorpg.core.administracao.DocumentoAnexo</class>
                        <class>fab.mentorpg.core.administracao.DocumentoRelatorioAnual</class>
                        <class>fab.mentorpg.core.administracao.DocumentoUsuario</class>
                        <class>fab.mentorpg.core.administracao.Instituicao</class>
                        <class>fab.mentorpg.core.administracao.LegislacaoEModelos</class>
                        <class>fab.mentorpg.core.administracao.Mensagem</class>
                        <class>fab.mentorpg.core.administracao.OcorrenciaCandidato</class>
                        <class>fab.mentorpg.core.administracao.OdsaInteressada</class>
                        <class>fab.mentorpg.core.administracao.OrgaoInstitucional</class>
                        <class>fab.mentorpg.core.administracao.QuestoesInstitucionais</class>
                        <class>fab.mentorpg.core.administracao.RelatorioAnual</class>
                        <class>fab.mentorpg.core.administracao.TipoDocumentacao</class>
                                
                        <class>fab.mentorpg.core.agenda.AbstractGeradorEventos</class>
                        <class>fab.mentorpg.core.agenda.AulaAluno</class>
                        <class>fab.mentorpg.core.agenda.Aulas</class>
                        <class>fab.mentorpg.core.agenda.EventoAdministrativo</class>
                        <class>fab.mentorpg.core.agenda.EventoBasico</class>
                        <class>fab.mentorpg.core.agenda.EventoPessoal</class>
                                
                        <class>fab.mentorpg.core.financeiro.Atividade</class>
                        <class>fab.mentorpg.core.financeiro.Caixa</class>
                        <class>fab.mentorpg.core.financeiro.ControleCredito</class>
                        <class>fab.mentorpg.core.financeiro.CreditoDeCaixa</class>
                        <class>fab.mentorpg.core.financeiro.DespachoProcessoFinanceiro</class>
                        <class>fab.mentorpg.core.financeiro.DocumentoFinanceiro</class>
                        <class>fab.mentorpg.core.financeiro.Meta</class>
                        <class>fab.mentorpg.core.financeiro.NaturezaCredito</class>
                        <class>fab.mentorpg.core.financeiro.NotaFinanceira</class>
                        <class>fab.mentorpg.core.financeiro.PlanejamentoFinanceiroAnual</class>
                                
                        <class>fab.mentorpg.core.planejamentoestrategico.AbstractEnquadramentoSelecionado</class>
                        <class>fab.mentorpg.core.planejamentoestrategico.ConfiguracaoTipoEnquadramento</class>
                        <class>fab.mentorpg.core.planejamentoestrategico.EnquadramentoSelecionado</class>
                        <class>fab.mentorpg.core.planejamentoestrategico.ItemEnquadramento</class>
                        <class>fab.mentorpg.core.planejamentoestrategico.OrgaoDemandadoPesquisa</class>
                        <class>fab.mentorpg.core.planejamentoestrategico.PropostaOperacional</class>
                        <class>fab.mentorpg.core.planejamentoestrategico.TeseRelacionadaId</class>
                        <class>fab.mentorpg.core.planejamentoestrategico.TesesRelacionadas</class>
                                
                        <class>fab.mentorpg.core.sige.AnexoOcorrenciaSIGE</class>
                        <class>fab.mentorpg.core.sige.Atribuicao</class>
                        <class>fab.mentorpg.core.sige.Comissao</class>
                        <class>fab.mentorpg.core.sige.CreditoValor</class>
                        <class>fab.mentorpg.core.sige.Critica</class>
                        <class>fab.mentorpg.core.sige.Instituicao</class>
                        <class>fab.mentorpg.core.sige.ModeloDocumentoSIGE</class>
                        <class>fab.mentorpg.core.sige.OcorrenciaComissao</class>
                        <class>fab.mentorpg.core.sige.Simposio</class>
                                
                        <class>fab.mentorpg.core.pessoal.Aluno</class>
                        <class>fab.mentorpg.core.pessoal.AtuacaoProfissional</class>
                        <class>fab.mentorpg.core.pessoal.CadastroUnico</class>
                        <class>fab.mentorpg.core.pessoal.Candidato</class>
                        <class>fab.mentorpg.core.pessoal.Contato</class>
                        <class>fab.mentorpg.core.pessoal.DisciplinaJaCursadas</class>
                        <class>fab.mentorpg.core.pessoal.Endereco</class>
                        <class>fab.mentorpg.core.pessoal.Foto</class>
                        <class>fab.mentorpg.core.pessoal.Pesquisador</class>
                        <class>fab.mentorpg.core.pessoal.Professor</class>
                        <class>fab.mentorpg.core.pessoal.Usuario</class>
                                
                        <class>fab.mentorpg.web.pessoal.Preferencias</class>
                                
                        <exclude-unlisted-classes>true</exclude-unlisted-classes>
                        <properties>
                            <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver" />
                            <!-- Propriedades para conexão com o banco de dados -->
                            <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/sucoi" />
                            <property name="jakarta.persistence.jdbc.user" value="appppgao" />
                            <property name="jakarta.persistence.jdbc.password" value="!@#$5678QWERTyuiop" />
                            <property name="hibernate.default_schema" value="mentorpg" />
                            <!-- Configurações do Hibernate -->
                            <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQL95Dialect" />
                            <!--<property name="hibernate.archive.autodetection" value="class, hbm"/>-->
                            <property name="hibernate.show_sql" value="false" /> <!-- Show SQL in console -->
                            <property name="hibernate.format_sql" value="false" /> <!-- Show SQL formatted -->
                            <property name="tomee.jpa.factory.lazy" value="true"/>
                            <property name="hibernate.hbm2ddl.auto" value="update" />  <!--create / create-drop / update -->
                        </properties>
                    </persistence-unit>
                                
                    <persistence-unit name="mentorpg3_PU" transaction-type="JTA">
                        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>            
                        <jta-data-source>jdbc/mentorpg3DS</jta-data-source>         
                                                                          
                """;

                StringBuilder part2 = new StringBuilder();
                List<String> lista = classesMigradas.stream().map(c -> "fab.mentorpg.migration." + c.getSimpleName()).sorted().toList();
                lista.forEach(c -> part2.append("\r\n\t\t<class>").append(c).append("</class>"));
                String part3 = """
                                <exclude-unlisted-classes>true</exclude-unlisted-classes>
                                <properties>
                                    <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver" />
                                    <!-- Propriedades para conexão com o banco de dados -->
                                    <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:15432/mentor" />
                                    <property name="jakarta.persistence.jdbc.user" value="mentor_app_access" />
                                    <property name="jakarta.persistence.jdbc.password" value="123456" />
                                    <property name="hibernate.default_schema" value="mentorpgapi" />
                                    <!-- Configurações do Hibernate -->
                                    <property name="hibernate.dialect" value="org.hibernate.dialect.PostgreSQL95Dialect" />
                                    <!--<property name="hibernate.archive.autodetection" value="class, hbm"/>-->
                                    <property name="hibernate.show_sql" value="false" /> <!-- Show SQL in console -->
                                    <property name="hibernate.format_sql" value="false" /> <!-- Show SQL formatted -->
                                    <property name="tomee.jpa.factory.lazy" value="true"/>
                                    <property name="hibernate.hbm2ddl.auto" value="update" />  <!--create / create-drop / update -->
                                </properties>
                                                
                            </persistence-unit>
                           \s
                        </persistence>\n
                        """;

        try {
            targetDir = targetDir.replace("java/fab/mentorpg/migration/","resources/META-INF/");
            BufferedWriter writer = new BufferedWriter(new FileWriter(targetDir + "persistence.xml"));
            writer.write(part1);
            writer.write(part2.toString());
            writer.write(part3);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
