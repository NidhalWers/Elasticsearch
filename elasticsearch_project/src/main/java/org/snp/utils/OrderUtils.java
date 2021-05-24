package org.snp.utils;

import org.snp.indexage.Table;
import org.snp.model.credentials.AggregateCredentials;
import org.snp.model.credentials.QueryCredentials;

import javax.enterprise.context.ApplicationScoped;
import java.util.*;

@ApplicationScoped
public class OrderUtils {

    public List<String> orderForColumn(Table table, List<AggregateCredentials> columnsSelected, List<QueryCredentials.OrderCredentials> orderCredentialsList, int orderColumnIndice,  List<String> listToSort){
        /**
         * if no other column to order by
         */
        QueryCredentials.OrderCredentials orderCredentials;
        if( (orderCredentials = orderCredentialsList.get(orderColumnIndice) ) == null )
            return listToSort;

        List<String> result = new ArrayList<>();

        /**
         * using map to Order
         */
        String columnName = orderCredentials.columnName;
        int position;
        int indiceInAggregate;
        if( columnsSelected!=null && (indiceInAggregate = indiceInAggregateCredentialsList(columnsSelected, columnName)) != -1 ){
            position = indiceInAggregate;
        }else{
            position = table.positionOfColumn(columnName);
        }
        Map<String,List<String>> mapOrderingResult = mapToOrder(position, listToSort, orderCredentialsList.get(orderColumnIndice).order);

        /**
         * create the list
         */
        for(Map.Entry<String, List<String>> entry : mapOrderingResult.entrySet()){
            List<String> lines;
            if( (lines = entry.getValue() ).size() == 1 ){
                /**
                 * add directly
                 */
                result.add(lines.get(0));
            }else{
                /**
                 * sort with the next column
                 */
                result.addAll( orderForColumn(table, columnsSelected, orderCredentialsList, orderColumnIndice++, lines) );
            }
        }

        return result;
    }

    private Map<String, List<String>> mapToOrder(int indiceColumn, List<String> list, QueryCredentials.OrderDirection orderDirection){
        Map<String, List<String>> result;
        if(QueryCredentials.OrderDirection.ASC.getValue().equals(orderDirection.getValue())){
            result = new TreeMap<>();
        }else{
            result = new TreeMap<>(Collections.reverseOrder());
        }

        String key;
        List<String> linesForKey;
        for(String line : list){
            key = getValueInPosition(indiceColumn, line);
            if((linesForKey = result.get(key)) == null){
                result.put(key, List.of(line));
            }else{
                linesForKey.add(line);
            }
        }

        return result;
    }



    private int indiceInAggregateCredentialsList(List<AggregateCredentials> aggregateCredentialsList, String word){
        int i=0;
        for(AggregateCredentials aggregateCredentials1 : aggregateCredentialsList){
            if(aggregateCredentials1.columnName.equals(word))
                return i;
            i++;
        }
        return -1;
    }

    private String getValueInPosition(int position, String line){
        String[] lineSplitted = line.split(",");
        return lineSplitted[ position ];
    }
}
