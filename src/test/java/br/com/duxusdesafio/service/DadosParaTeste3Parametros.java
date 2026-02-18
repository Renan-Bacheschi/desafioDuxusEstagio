package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DadosParaTeste3Parametros {

    // datas
    private final LocalDate data1993 = LocalDate.of(1993, 1, 1);
    private final LocalDate data1994 = LocalDate.of(1994, 1, 1);
    private final LocalDate data1995 = LocalDate.of(1995, 1, 1);

    // franquias
    private final String franquiaNBA = "NBA";


    // integrantes
    // nba
    private Integrante michael_jordan;
    private Integrante denis_rodman;
    private Integrante scottie_pippen;

    private Time timeChicagoBullsDe1994;
    private Time timeChicagoBullsDe1995;

    private Time timeDetroidPistonsDe1993;

    private List<Time> todosOsTimes;

    public DadosParaTeste3Parametros() {
        inicializa();
    }

    public void inicializa() {

        List<ComposicaoTime> composicaoTime1993 = new ArrayList<>();
        List<ComposicaoTime> composicaoTime1994 = new ArrayList<>();
        List<ComposicaoTime> composicaoTime1995 = new ArrayList<>();
        List<ComposicaoTime> composicaoTime1994E1995 = new ArrayList<>();

        // integrantes
        // nba
        michael_jordan = new Integrante(franquiaNBA, "Michael Jordan", "ala", composicaoTime1994E1995);
        denis_rodman = new Integrante(franquiaNBA, "Denis Rodman", "ala-pivô", composicaoTime1995);
        scottie_pippen = new Integrante(franquiaNBA, "Scottie Pippen", "ala", composicaoTime1995);

        // times
        timeChicagoBullsDe1994 = new Time("Chicago Bulls", data1994, composicaoTime1994);
        timeChicagoBullsDe1995 = new Time("Chicago Bulls", data1995, composicaoTime1995);
        timeDetroidPistonsDe1993 = new Time("Detroit Pistons", data1993, composicaoTime1993);

        // composição chicago bulls
        composicaoTime1994.add(new ComposicaoTime(timeChicagoBullsDe1994, michael_jordan));
        composicaoTime1994.add(new ComposicaoTime(timeChicagoBullsDe1994, denis_rodman));
        composicaoTime1994.add(new ComposicaoTime(timeChicagoBullsDe1994, scottie_pippen));

        composicaoTime1995.add(new ComposicaoTime(timeChicagoBullsDe1995, michael_jordan));
        composicaoTime1995.add(new ComposicaoTime(timeChicagoBullsDe1995, denis_rodman));
        composicaoTime1995.add(new ComposicaoTime(timeChicagoBullsDe1995, scottie_pippen));

        // composição detroid pistons
        composicaoTime1993.add(new ComposicaoTime(timeDetroidPistonsDe1993, denis_rodman));

        // Criar serviço ou lógica sempre com base na entidade Time!
        composicaoTime1994E1995.addAll(composicaoTime1994);
        composicaoTime1994E1995.addAll(composicaoTime1995);

        todosOsTimes = new ArrayList<>();
        todosOsTimes.add(timeChicagoBullsDe1994);
        todosOsTimes.add(timeChicagoBullsDe1995);
        todosOsTimes.add(timeDetroidPistonsDe1993);
    }

        // --- GETTERS E SETTERS ---

    public List<Time> getTodosOsTimes() {
        return todosOsTimes;
    }

    public void setTodosOsTimes(List<Time> todosOsTimes) {
        this.todosOsTimes = todosOsTimes;
    }

    public Time getTimeChicagoBullsDe1995() {
        return timeChicagoBullsDe1995;
    }

    public void setTimeChicagoBullsDe1995(Time timeChicagoBullsDe1995) {
        this.timeChicagoBullsDe1995 = timeChicagoBullsDe1995;
    }

    public Time getTimeChicagoBullsDe1994() {
        return timeChicagoBullsDe1994;
    }

    public void setTimeChicagoBullsDe1994(Time timeChicagoBullsDe1994) {
        this.timeChicagoBullsDe1994 = timeChicagoBullsDe1994;
    }

    public Time getTimeDetroidPistonsDe1993() {
        return timeDetroidPistonsDe1993;
    }

    public void setTimeDetroidPistonsDe1993(Time timeDetroidPistonsDe1993) {
        this.timeDetroidPistonsDe1993 = timeDetroidPistonsDe1993;
    }

    public String getFranquiaNBA() {
        return franquiaNBA;
    }

    public Integrante getDenis_rodman() {
        return denis_rodman;
    }

    public void setDenis_rodman(Integrante denis_rodman) {
        this.denis_rodman = denis_rodman;
    }

    public Integrante getMichael_jordan() {
        return michael_jordan;
    }

    public void setMichael_jordan(Integrante michael_jordan) {
        this.michael_jordan = michael_jordan;
    }

    public Integrante getScottie_pippen() {
        return scottie_pippen;
    }

    public void setScottie_pippen(Integrante scottie_pippen) {
        this.scottie_pippen = scottie_pippen;
    }
}