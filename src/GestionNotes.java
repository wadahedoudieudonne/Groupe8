
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Etudiant {
    private String nom;
    private Map<String, Double> notes;
    private double moyenneGenerale;
    private int rang;

    public Etudiant(String nom, Map<String, Double> notes) {
        this.nom = nom;
        this.notes = notes;
    }

    public String getNom() {
        return nom;
    }

    public Map<String, Double> getNotes() {
        return notes;
    }

    public double getMoyenneGenerale() {
        return moyenneGenerale;
    }

    public void setMoyenneGenerale(double moyenneGenerale) {
        this.moyenneGenerale = moyenneGenerale;
    }

    public int getRang() {
        return rang;
    }

    public void setRang(int rang) {
        this.rang = rang;
    }
}

public class GestionNotes {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Étape 1 : Saisir les matières et leurs coefficients
        Map<String, Integer> matieres = new HashMap<>();
        System.out.println("Entrez les matières et leurs coefficients (tapez 'fin' pour terminer) :");
        while (true) {
            System.out.print("Nom de la matière : ");
            String matiere = scanner.nextLine();
            if (matiere.equalsIgnoreCase("fin")) {
                break;
            }
            System.out.print("Coefficient de " + matiere + " : ");
            int coefficient = scanner.nextInt();
            scanner.nextLine(); // Pour consommer la nouvelle ligne
            matieres.put(matiere, coefficient);
        }

        // Étape 2 : Saisir les informations des étudiants
        List<Etudiant> etudiants = new ArrayList<>();
        while (true) {
            System.out.print("Entrez le nom de l'étudiant (ou 'fin' pour terminer) : ");
            String nom = scanner.nextLine();
            if (nom.equalsIgnoreCase("fin")) {
                break;
            }

            Map<String, Double> notes = new HashMap<>();
            for (Map.Entry<String, Integer> entry : matieres.entrySet()) {
                System.out.print("Entrez la note en " + entry.getKey() + " (sur 20) : ");
                double note = scanner.nextDouble();
                scanner.nextLine(); // Pour consommer la nouvelle ligne
                notes.put(entry.getKey(), note);
            }

            etudiants.add(new Etudiant(nom, notes));
        }

        // Étape 3 : Calculer la moyenne générale de chaque étudiant
        for (Etudiant etudiant : etudiants) {
            double totalNotes = 0;
            int totalCoefficients = 0;
            for (Map.Entry<String, Double> entry : etudiant.getNotes().entrySet()) {
                String matiere = entry.getKey();
                double note = entry.getValue();
                int coefficient = matieres.get(matiere);
                totalNotes += note * coefficient;
                totalCoefficients += coefficient;
            }
            double moyenneGenerale = totalNotes / totalCoefficients;
            etudiant.setMoyenneGenerale(moyenneGenerale);
        }

        // Étape 4 : Déterminer le rang de chaque étudiant
        etudiants.sort((e1, e2) -> Double.compare(e2.getMoyenneGenerale(), e1.getMoyenneGenerale()));
        for (int i = 0; i < etudiants.size(); i++) {
            etudiants.get(i).setRang(i + 1);
        }

        // Étape 5 : Générer le fichier CSV
        try (FileWriter writer = new FileWriter("rapport_etudiants.csv")) {
            // En-tête du fichier CSV
            writer.append("Nom");
            for (String matiere : matieres.keySet()) {
                writer.append(",").append(matiere);
            }
            writer.append(",Moyenne Générale,Rang\n");

            // Données des étudiants
            for (Etudiant etudiant : etudiants) {
                writer.append(etudiant.getNom());
                for (String matiere : matieres.keySet()) {
                    writer.append(",").append(String.valueOf(etudiant.getNotes().get(matiere)));
                }
                writer.append(",").append(String.valueOf(etudiant.getMoyenneGenerale()));
                writer.append(",").append(String.valueOf(etudiant.getRang()));
                writer.append("\n");
            }

            System.out.println("Le rapport a été généré avec succès dans le fichier 'rapport_etudiants.csv'.");
        } catch (IOException e) {
            System.out.println("Une erreur est survenue lors de la génération du fichier CSV.");
            e.printStackTrace();
        }

        scanner.close();
    }
}