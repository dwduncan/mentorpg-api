package mil.decea.mentorpgapi.domain.user.validation.annotations;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;

public class CpfConstraint implements ConstraintValidator<IsValidCpf, String> {
    @Override
    public boolean isValid(String cpf,
                           ConstraintValidatorContext cxt) {
        return isValidCpf(cpf);
    }

    public static boolean isValidCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) return false;

        try{
            Long.parseLong(cpf);
        } catch(NumberFormatException e){

            return false;
        }

        int d1 = 0, d2 = 0;
        int digito1 = 0, digito2 = 0, resto;
        int digitoCPF;
        String nDigResult;

        for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
            digitoCPF = Integer.parseInt(cpf.substring(nCount - 1, nCount));

            // multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4
            // e assim por diante.
            d1 = d1 + (11 - nCount) * digitoCPF;

            // para o segundo digito repita o procedimento incluindo o primeiro
            // digito calculado no passo anterior.
            d2 = d2 + (12 - nCount) * digitoCPF;
        }
        // Primeiro resto da divisão por 11.
        resto = (d1 % 11);

        // Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11
        // menos o resultado anterior.
        if (resto >= 2) digito1 = 11 - resto;

        d2 += 2 * digito1;

        // Segundo resto da divisão por 11.
        resto = (d2 % 11);

        // Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11
        // menos o resultado anterior.
        if (resto >= 2) digito2 = 11 - resto;

        // Digito verificador do CPF que está sendo validado.
        String nDigVerific = cpf.substring(cpf.length() - 2);

        // Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + digito2;

        // comparar o digito verificador do cpf com o primeiro resto + o segundo
        // resto.
        return nDigVerific.equals(nDigResult);
    }


}
