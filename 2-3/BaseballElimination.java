import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseballElimination {
    private final int numberOfTeams;
    private final Map<String, Integer> teamToIndex;
    private final String[] teams;
    private final int[] w;
    private final int[] l;
    private final int[] r;
    private final int[][] g;

    private final int gameVertices;
    private final int teamVertices;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In file = new In(filename);
        numberOfTeams = file.readInt();

        gameVertices = (numberOfTeams - 2) * (numberOfTeams - 1) / 2;
        teamVertices = numberOfTeams - 1;

        teamToIndex = new HashMap<>(); 
        teams = new String[numberOfTeams];
        w = new int[numberOfTeams];
        l = new int[numberOfTeams];
        r = new int[numberOfTeams];
        g = new int[numberOfTeams][numberOfTeams];

        for (int i = 0; i < numberOfTeams; i++) {
            String name = file.readString(); 
            teams[i] = name;
            teamToIndex.put(name, i);

            w[i] = file.readInt();
            l[i] = file.readInt();
            r[i] = file.readInt();
            
            for (int j = 0; j < numberOfTeams; j++) {
                g[i][j] = file.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teamToIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        return w[teamToIndex.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teamToIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        return l[teamToIndex.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teamToIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        return r[teamToIndex.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamToIndex.containsKey(team1) || !teamToIndex.containsKey(team2)) {
            throw new java.lang.IllegalArgumentException();
        }

        int teamIndex1 = teamToIndex.get(team1);
        int teamIndex2 = teamToIndex.get(team2);

        return g[teamIndex1][teamIndex2];
    }

    // is given team eleminated?
    public boolean isEliminated(String team) {
        if (!teamToIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        List<String> otherTeams = otherTeams(team);

        int teamMaxWins = wins(team) + remaining(team);
        for (String otherTeam : otherTeams) {
            int otherTeamMaxWins = wins(otherTeam);
            
            if (otherTeamMaxWins > teamMaxWins) {
                return true;
            }
        }

        FordFulkerson ff = solveMaxProblem(team, otherTeams);
        for (int i = 1; i < 1 + gameVertices; i++) {
            if (ff.inCut(i)) {
                return true;
            }
        }
        
        return false;
    }

    // subset R of teams that eliminates given team; null if not eleminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamToIndex.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }

        List<String> subset = new ArrayList<String>();
        List<String> otherTeams = otherTeams(team);

        int teamMaxWins = wins(team) + remaining(team);
        for (String otherTeam : otherTeams) {
            int otherTeamMaxWins = wins(otherTeam);
            
            if (otherTeamMaxWins > teamMaxWins) {
                subset.add(otherTeam);
                return subset;
            }
        }

        FordFulkerson ff = solveMaxProblem(team, otherTeams);
        for (int i = 1 + gameVertices; i < 1 + gameVertices + teamVertices; i++) {
            if (ff.inCut(i)) {
                subset.add(otherTeams.get(i - 1 - gameVertices)); 
            }
        }

        if (subset.isEmpty()) {
            return null;
        }

        return subset;
    }

    private FordFulkerson solveMaxProblem(String team, List<String> otherTeams) {
        int source = 0;
        int sink = 1 + gameVertices + teamVertices;
        FlowNetwork fn = new FlowNetwork(1 + gameVertices + teamVertices + 1);

        int nextVertice = 1;
        for (int i = 0; i < numberOfTeams - 1; i++) {
            for (int j = i + 1; j < numberOfTeams - 1; j++) {
                fn.addEdge(new FlowEdge(source, nextVertice, 
                        against(otherTeams.get(i), otherTeams.get(j))));

                fn.addEdge(new FlowEdge(nextVertice,
                            1 + gameVertices + i,
                            Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(nextVertice,
                            1 + gameVertices + j,
                            Double.POSITIVE_INFINITY));

                nextVertice++;
            }

            int canStillWin = wins(team) + remaining(team) - wins(otherTeams.get(i));
            if (canStillWin < 0) {
                canStillWin = 0;
            }
            fn.addEdge(new FlowEdge(1 + gameVertices + i, sink, canStillWin));
        }

        return new FordFulkerson(fn, source, sink); 
    }

    private List<String> otherTeams(String team) {
        List<String> otherTeams = new ArrayList<>(numberOfTeams - 1);
        for (int i = 0; i < teams.length; i++) {
            if (!team.equals(teams[i])) {
                otherTeams.add(teams[i]); 
            }
        }

        return otherTeams;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
