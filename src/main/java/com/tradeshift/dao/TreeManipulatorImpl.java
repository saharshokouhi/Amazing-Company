package com.tradeshift.dao;


import com.tradeshift.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class TreeManipulatorImpl implements TreeManipulator {

    @Autowired
    NodeRepository nodeRepository;

    @Transactional
    public void changeParent(Node source, Node destination) {
        int heightDiff = destination.getHeight() - source.getHeight() + 1;
        int width = source.getRightPos() - source.getLeftPos() + 1;
        int newLeftPosition = destination.getLeftPos() + 1;
        int distance = newLeftPosition - source.getLeftPos();

        int tmpPos = source.getLeftPos();
        int oldRightPosition = source.getRightPos();

        if (distance < 0) {
            distance -= width;
            tmpPos += width;
        }
        nodeRepository.updateLeft(newLeftPosition, width);
        nodeRepository.updateRight(newLeftPosition, width);

        nodeRepository.moveSubtree(width, distance, tmpPos, heightDiff);
        nodeRepository.updateParent(source.getId(), destination.getId());

        nodeRepository.removeLeftSpace(width, oldRightPosition);
        nodeRepository.removeRightSpace(width, oldRightPosition);
    }

    @Transactional
    @Override
    public void saveTree(Node node){
        saveNode(node);
    }

    private void saveNode(Node node) {
        nodeRepository.save(node);
        node.getChildren().ifPresent(children -> children.forEach(child -> saveNode(child)));
    }
}
