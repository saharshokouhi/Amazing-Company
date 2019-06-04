package com.tradeshift;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeshift.dao.NodeRepository;
import com.tradeshift.dao.TreeManipulator;
import com.tradeshift.model.Node;
import com.tradeshift.service.GraphServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;


@RunWith(MockitoJUnitRunner.class)
public class GraphServiceTest {

    @InjectMocks
    GraphServiceImpl graphService;

    @Mock
    NodeRepository nodeRepositoryMock;

    @Mock
    TreeManipulator treeManipulator;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void insertNewTest() throws IOException {
        Node rootNode = prepareForInsert("graph/normal_1.json");
        graphService.insertNew(rootNode);
    }
    @Test
    public void insertNewTestLeftPosAndRightPos() throws IOException {
        Node rootNode = prepareForInsert("graph/normal_1.json");
        graphService.insertNew(rootNode);
        Assert.assertEquals(rootNode.getLeftPos(),1);
        Assert.assertEquals(rootNode.getRightPos(),10);
        List<Node> rootChildren = rootNode.getChildren().orElse(new ArrayList<>());
        Assert.assertEquals(rootChildren.size(),3);
        Assert.assertEquals(rootChildren.get(2).getLeftPos(),6);
        Assert.assertEquals(rootChildren.get(2).getRightPos(),9);
    }


    @Test(expected = IllegalArgumentException.class)
    public void insertNewTestDuplicateId() throws IOException {
        Node rootNode = prepareForInsert("graph/dup_id.json");
        graphService.insertNew(rootNode);
    }

    private Node prepareForInsert(String resourcePath) throws IOException {
        Node rootNode = loadTree(resourcePath);
        doNothing().when(nodeRepositoryMock).deleteTree();
        doNothing().when(treeManipulator).saveTree(rootNode);
        return rootNode;
    }
    private Node loadTree(String resourcePath) throws IOException {
        File resource = new ClassPathResource(resourcePath).getFile();
        ObjectMapper mapper = new ObjectMapper();
         return mapper.readValue(resource, Node.class);
    }
    @Test(expected = IllegalArgumentException.class)
    public void changeParentTestForSameSourceAndDestinationId() {
        graphService.changeParent(1l,1l);
    }


}
