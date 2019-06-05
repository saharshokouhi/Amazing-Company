package com.tradeshift.dao;

import com.tradeshift.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {


    @Query(("SELECT node FROM  Node node where node.parent.id = :parentId"))
    List<Node> getChildren(@Param("parentId") Long parentId);

    @Transactional
    @Modifying
    @Query("UPDATE Node node SET node.leftPos = node.leftPos + :width  WHERE node.leftPos >= :newPos")
    void updateLeft(@Param("newPos") int newPos, @Param("width") int width);

    @Transactional
    @Modifying
    @Query("UPDATE Node node SET node.rightPos = node.rightPos + :width  WHERE node.rightPos >= :newPos")
    void updateRight(@Param("newPos") int newPos, @Param("width") int width);

    @Transactional
    @Modifying
    @Query("UPDATE Node node " +
            "SET node.leftPos = node.leftPos + :distance ," +
            " node.rightPos = node.rightPos + :distance ," +
            " node.height = node.height + :heightDiff " +
            "WHERE node.leftPos >= :tmpPos AND node.rightPos < :tmpPos + :width ")
    void moveSubtree(@Param("width") int width, @Param("distance") int distance, @Param("tmpPos") int tmpPos, @Param("heightDiff") int heightDiff);

    @Transactional
    @Modifying
    @Query("UPDATE Node node SET node.parent.id = :newParentId  WHERE node.id = :id")
    void updateParent(@Param("id") Long id,@Param("newParentId") Long newParentId);

    @Transactional
    @Modifying
    @Query("UPDATE Node node SET node.leftPos = node.leftPos - :width  WHERE node.leftPos > :oldRightSpace")
    void removeLeftSpace(@Param("width") int width, @Param("oldRightSpace") int oldRightSpace);

    @Transactional
    @Modifying
    @Query("UPDATE Node node SET node.rightPos = node.rightPos - :width  WHERE node.rightPos > :oldRightSpace ")
    void removeRightSpace(@Param("width") int width, @Param("oldRightSpace") int oldRightSpace);

    @Transactional
    @Modifying
    @Query("DELETE FROM Node node")
    void deleteTree();

    @Query("SELECT node FROM Node node where node.parent.id is null")
    Node getRoot();
}
