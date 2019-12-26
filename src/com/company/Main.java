package com.company;

import java.util.*;

class Main
{

    // Number of processes
    static Random rd = new Random();
    static Scanner scanner = new Scanner(System.in);
    static int P =2+ rd.nextInt(5); // 5

    // Number of resources
    static int R =2+ rd.nextInt(5); // 3

    // Function to find the need of each process
    static void calculateNeed(int need[][], int maxm[][], int allot[][])
    {
        // Calculating Need of each Process
        for (int i = 0 ; i < P ; i++) {
            for (int j = 0; j < R; j++) {

                // Need of instance = maxm instance - allocated instance
                need[i][j] = maxm[i][j] - allot[i][j];
            }
        }

    }

    // Function to find the systfem is in safe state or not
    static boolean isSafe(int avail[], int maxm[][], int allot[][])
    {
        int [][]need = new int[P][R];

        System.out.println("Total: " + P + " process");
        System.out.println("Total: " + R + " type of resources");
        System.out.println("Allot Matrix: ");
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                System.out.print(allot[i][j] + "\t");
            }

            System.out.println("\n");
        }
        System.out.println("Max Matrix: ");
        for (int i = 0; i < P; i++) {
            for (int j = 0; j < R; j++) {
                System.out.print(maxm[i][j] + "\t");
            }

            System.out.println("\n");
        }

        System.out.println("Avai Matrix: ");
        for (int i = 0; i < R; i++) {
            System.out.print(avail[i] + "\t");
        }
        System.out.println("\n");


        // Function to calculate need matrix
        calculateNeed(need, maxm, allot);

        // Mark all processes as infinish
        boolean []finish = new boolean[P];

        // To store safe sequence
        int []safeSeq = new int[P];

        // Make a copy of available resources

        int []work = new int[R];
        for (int i = 0; i < R ; i++){
            work[i] = avail[i];
        }

        // While all processes are not finished
        // or system is not in safe state.
        int count = 0;
        while (count < P)
        {
            // Find a process which is not finish and
            // whose needs can be satisfied with current
            // work[] resources.
            boolean found = false;
            for (int p = 0; p < P; p++)
            {
                // First check if a process is finished,
                // if no, go for next condition


                if (finish[p] == false)
                {
                    // Check if for all resources of
                    // current P need is less
                    // than work
                    int j;
                    for (j = 0; j < R; j++)
                        if (need[p][j] > work[j])
                            break;

                    // If all needs of p were satisfied.
                    if (j == R) // điều kiện j == R để kiểm tra rằng khi đã kiểm tra qua tất cả need về resource của tiến trình thì đến bước này
                    {
                        // Add the allocated resources of
                        // current P to the available/work
                        // resources i.e.free the resources
                        for (int k = 0 ; k < R ; k++) // Khi đã kiểm tra rằng lượng avai hiện tại đủ cung cấp cho need của process thì chúng ta giải phóng tiến trình
                            work[k] += allot[p][k];

                        // Add this process to safe sequence.
                        // Cho thứ tự của process vào mục là đã hoàn thành
                        safeSeq[count] = p;
                        count += 1; // chạy vòng tiếp theo

                        // Mark this p as finished
                        // đánh dấu process đã hoàn thành để khi lặp lại sẽ bỏ qua tiến trình này
                        finish[p] = true;

                        found = true; // nếu không trả ra được giá trị true này thì nghĩa là có ít nhất 1 tiến trình không hoàn thành => không an toàn
                    }
                }
            }

            // If we could not find a next process in safe
            // sequence.
            if (found == false)
            {
                System.out.println("System is not in safe state");
                return false;
            }
        }

        // If system is in safe state then
        // safe sequence will be as below
        System.out.print("System is in safe state.\nSafe sequence is: ");
        for (int i = 0; i < P ; i++)
            System.out.print(safeSeq[i] + " ");
        System.out.println("\n");
        return true;
    }

    // Driver code
    public static void main(String[] args)
    {
        int[] numberOfResources = new int[R];
        int[] availOfResources = new int[R];
        int [][] Allot = new int[P][R];
        int[] totalAllot = new int[R];
        int [][] MaxRequire = new int[P][R];

        for (int i = 0; i < P ; i++) {
            for (int j = 0; j < R ; j++) {
                Allot[i][j] = rd.nextInt(10);
                MaxRequire[i][j] = Allot[i][j] + rd.nextInt(20);
            }
        }


        for (int i = 0; i < P ; i++) {
            for (int j = 0; j < R ; j++) {
                totalAllot[j] += Allot[i][j];
            }
        }


        for (int i = 0; i < R ; i++) {
            availOfResources[i] = rd.nextInt(10);
            numberOfResources[i] = availOfResources[i] + totalAllot[i];
        }


        int avail[] = numberOfResources;

        int maxm[][] = MaxRequire;


        int allot[][] = Allot;

        // Check system is in safe state or not
        System.out.println("Safety algorithm");
        boolean isSafeState = isSafe(avail, maxm, allot);

        System.out.print("Do you want to execute Resource request algorithm ? (1/0): ");
        int userChoice = scanner.nextInt();
        if(userChoice != 1) System.out.println("Bai baii");
        while (isSafeState && userChoice == 1){
            System.out.println("------------------------------------");
            System.out.println("Resource request algorithm");
            int requestProcess = rd.nextInt(P);
            int [] request = new int[R];
            for (int i = 0; i < R ; i++) {
                request[i] = rd.nextInt(5);
            }
            System.out.print("Process number: " + requestProcess + " request more resource: ");
            for (int i = 0; i < R ; i++) {
                System.out.print(" " + request[i]);
            }
            System.out.println("\n");
            //STEP 1: check if request <= need
            boolean lock = false;
            for (int i = 0; i < R ; i++) {
                if ( request[i] > (maxm[requestProcess][i] - allot[requestProcess][i]) ){
                    lock = true;
                    System.out.println("STEP 1: FALSE");
                    System.out.println("At resource number " +i + " request: " +request[i] + " > " + " need: " + (maxm[requestProcess][i] - allot[requestProcess][i]));
                    break;
                }
            }
            if(lock){break;}
            //STEP 2: check if request <= available
            for (int i = 0; i < R ; i++) {
                if ( request[i] >  avail[i] ){
                    lock = true;
                    System.out.println("STEP 2: FALSE");
                    System.out.println("At resource number " +i + " request: " +request[i] + " > " + " avail: " + avail[i]);
                    break;
                }
            }
            if(lock){break;}
            //STEP 3:
            for (int i = 0; i < R ; i++) {
                avail[i] -= request[i];
                allot[requestProcess][i] += request[i];
            }
            //STEP 4 : RUN SAFETY ALGORITHM
            isSafe(avail, maxm, allot);
            break;
        }
    }
}
