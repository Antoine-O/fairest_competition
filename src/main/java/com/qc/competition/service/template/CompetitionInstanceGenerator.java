package com.qc.competition.service.template;

import com.qc.competition.service.structure.CompetitionInstance;

import java.util.List;

/**
 * Created by Duncan on 02/06/2015.
 */
public interface CompetitionInstanceGenerator {


    List<CompetitionInstance> computeCompetitionFormatFor(CompetitionComputationParam competitionComputationParam, int sizeResult) throws CompetitionInstanceGeneratorException;

    CompetitionInstance createCompetitionInstanceFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException;

    CompetitionInstance computeCompetitionFormatFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException;

    CompetitionInstance createCompetitionInstanceEliminationFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException;

    CompetitionInstance createCompetitionInstanceRoundRobinFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException;

    CompetitionInstance createCompetitionInstanceSwissFor(CompetitionComputationParam competitionComputationParam) throws CompetitionInstanceGeneratorException;
}
