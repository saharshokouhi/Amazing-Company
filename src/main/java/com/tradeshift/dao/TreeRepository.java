package com.tradeshift.dao;

import com.tradeshift.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreeRepository extends JpaRepository<Node,Long> {

    @Query("SELECT node FROM  Node node WHERE node.parent.id = ?1")
    List<Node> getChildren(Long parentId);

    @Query("UPDATE Node node SET node.leftPos = node.leftPos + ?2  WHERE node.leftPos >= ?1")
    void updateLeft(int newPos, int width);

    @Query("UPDATE Node node SET node.rightPos = node.rightPos + ?2  WHERE node.rightPos >= ?1")
    void updateRight(int newPos, int width);

    @Query("UPDATE Node node " +
            "SET node.leftPos = node.leftPos + ?2, node.rightPos = node.rightPos + ?2  " +
            "WHERE node.leftPos >= ?3 AND node.rightPos < ?3 + ?1 ")
    void moveSubtree(int width, int distance , int tmpPos);
}
