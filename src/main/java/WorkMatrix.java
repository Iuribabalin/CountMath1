
public class WorkMatrix {
    public int n;
    public double e;
    public float[][] matrix;
    private final float[] answers_x;
    private float[] approaching;
    int iteration_method = 0;

    WorkMatrix(int n, double e){
        this.n = n;
        this.e = e;
        this.matrix = new float[n][n+1];
        this.answers_x = new float[n];
        this.approaching = new float[n];
    }

    private void outMatrix(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.printf("%.2f\t", this.matrix[i][j]);
            }
            System.out.printf("|%.2f\n", this.matrix[i][n]);
        }

        System.out.println("--------------------------------");
    }

    private void outMatrixOdds(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                System.out.printf("%.2f\t", this.matrix[i][j]);
            }
            System.out.println("");
        }
        System.out.println("--------------------------------");
    }

    private void outMatrixAnswers(){
        for(int i = 0; i < n; i++){
            System.out.printf("%.2f\t", this.matrix[i][n]);
        }
        System.out.println("\n--------------------------------");
    }

    private void outApproaching(){
        for(int i = 0; i < n; i++){
            System.out.printf("%.2f\t", this.approaching[i]);
        }
        System.out.println("\n--------------------------------");
    }

    private void outAnswers_X(){
        System.out.println("Ответы для нашего СЛАУ");
        for(int i = 0; i < n; i++){
            System.out.printf("%.2f\t", this.answers_x[i]);
        }
        System.out.println("\n--------------------------------");
    }

    private void outApproachingLast(){
        for(int i = 0; i < n; i++){
            System.out.printf("%.10f\t", this.approaching[i]);
        }
        System.out.println("\n--------------------------------");
    }

    public void startIterativeMethod(){
        System.out.println("--------------------------------" +
                "\nНаша точность: " + this.e);
        System.out.println("Вывод начальной матрицы");

        outMatrix();

        System.out.println("Результаты работы программы");

        if(doDiagonalMatrix()) {
            System.out.println("Вывод матрицы после приведения диагоналей");
            outMatrix();

            System.out.println("Матрица коэффициентов преобразованной системы");

            setMatrixOdds();
            inverseOdds();
            outMatrixOdds();

            System.out.println("Вектор правых частей преобразованной системы");

            outMatrixAnswers();

            if(normalizeMatrix() < 1) {

                System.out.println("Зададим начальное приближение");

                setStartApproaching();

                outApproaching();

                setStartAnswers_X();

                doMethod();

                outAnswers_X();

                System.out.println("Колчество итераций алгоритма: " + iteration_method +
                        "\n--------------------------------");

                System.out.println("Конечные погрешности");

                outApproachingLast();
            }else{
                System.out.println("Условие сходимости не выполнено");
            }

        }else{
            System.out.println("Невозможно достичь диагонального преобладания");
        }
    }

    private void swapLine(int index, int index_swap){
        for(int j = 0; j < n+1; j++){
            float tmp = matrix[index][j];
            matrix[index][j] = matrix[index_swap][j];
            matrix[index_swap][j] = tmp;
        }
    }

    private boolean checkingDiagonalElement(float element, int index, int j_index){
        float sum = 0;
        for(int j = 0; j < n; j++){
            if(j != j_index){
                sum += Math.abs(matrix[index][j]);
            }
        }
        return element > sum;
    }

    private boolean doDiagonalMatrix(){
        int iteration = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(checkingDiagonalElement(Math.abs(matrix[i][j]), i, j)){
                    swapLine(i,j);
                    iteration++;
                }
            }
        }
        return iteration > 1;
    }

    private void setMatrixOdds(){
        for(int i = 0; i < n; i++){
            float divider = matrix[i][i];
            matrix[i][i] = 0;
            for(int j = 0; j < n+1; j++){
                matrix[i][j] = matrix[i][j]/divider;
            }
        }
    }

    private void inverseOdds(){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i != j)
                    matrix[i][j] = -1 * matrix[i][j];
            }
        }
    }

    private float normalizeMatrix(){
        float max_norm = 0;
        for(int i = 0; i < n; i++){
            float sum = 0;
            for(int j = 0; j < n; j++){
                sum += -1 * matrix[i][j];
            }
            if(sum > max_norm)
                max_norm = sum;
        }

        System.out.printf("Норма преобразованной матрицы: %.2f\n", max_norm);

        if(max_norm < 1){
            System.out.println("Условие сходимости выполнено");
        }

        return max_norm;
    }

    private void setStartAnswers_X(){
        for(int i = 0; i < n; i++){
            answers_x[i] = matrix[i][n];
        }
    }

    private void setStartApproaching(){
        for(int i = 0; i < n; i++){
            approaching[i] = matrix[i][n];
        }
    }


    private float maxApproaching(){
        float max = 0;
        for(int i = 0; i < n; i++){
            if(max < Math.abs(approaching[i]))
             max = Math.abs(approaching[i]);
        }
        return max;
    }

    private void doMethod(){
        float max_approaching_do_method = maxApproaching();
        while(e < Math.abs(max_approaching_do_method)){
            float[] stack_answers_x = new float[n];

            for(int i = 0; i < n; i++){
                float answer_x = 0;
                for(int j = 0; j < n; j++){
                    if(i != j){
                        answer_x += matrix[i][j] * answers_x[j];
                    }
                }
                answer_x += matrix[i][n];
                approaching[i] = Math.abs(answers_x[i] - answer_x);
                stack_answers_x[i] = answer_x;
            }

            if (n >= 0) System.arraycopy(stack_answers_x, 0, answers_x, 0, n);

            max_approaching_do_method = maxApproaching();
            iteration_method++;
        }
    }




}
