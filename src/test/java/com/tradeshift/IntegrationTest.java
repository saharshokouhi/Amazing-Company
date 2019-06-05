package com.tradeshift;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    private MockMvc mvc;


    /**
     * this method does these steps
     * 1- insert new tree from json tree resource
     * 2- get children node with id 1, check response status , check it's children and check height child with node id 2
     * 3- change parent node 2 from root node to node 4
     * 4- do get children again and check if response is ok and new location's of node 2 and also it's new height
     */
    @Test
    public void insertNew_getChildren_ChangeParent_TestHeight()
            throws Exception {
        insertNewTree("tree/normal_1.json");
        List<Map> children =getChildren(1L);
        Assert.assertEquals(children.get(0).get("id"), 2);
        Assert.assertEquals(children.get(0).get("height"), 1);

        Assert.assertEquals(children.get(1).get("id"), 3);
        Assert.assertEquals(children.get(2).get("id"), 4);

        changeParent(2L,4L);

        children = getChildren(4L);
        Assert.assertEquals(children.get(0).get("id"), 2);
        Assert.assertEquals(children.get(0).get("height"), 2);
    }
    @Test
    public void heightCalculationTest()
            throws Exception {
        insertNewTree("tree/normal_1.json");


        List<Map> rootChildren =getChildren(1L);

        Assert.assertEquals(rootChildren.get(0).get("id"), 2);
        Assert.assertEquals(rootChildren.get(0).get("height"), 1);

        Assert.assertEquals(rootChildren.get(1).get("id"), 3);
        Assert.assertEquals(rootChildren.get(1).get("height"), 1);

        Assert.assertEquals(rootChildren.get(2).get("id"), 4);
        Assert.assertEquals(rootChildren.get(1).get("height"), 1);

        List<Map> node4Children =getChildren(4L);
        Assert.assertEquals(node4Children.get(0).get("id"), 5);
        Assert.assertEquals(node4Children.get(0).get("height"), 2);
    }


    private void changeParent(Long sourceId,Long destinationId) throws Exception {
        mvc.perform(patch(String.format("/tree/%s/%s",sourceId,destinationId)))
                .andExpect(status().is2xxSuccessful());
    }
    private List<Map> getChildren(long parentId) throws Exception {
        String result = mvc.perform(get(String.format("/tree/%s/children",parentId)))
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result, List.class);
    }

    @Test
    public void insertEmptyTreeTest() throws Exception {
        String payload = readTree("tree/empty.json");
        mvc.perform(put("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is5xxServerError());
    }

    private String readTree(String filePath) throws IOException {
        File resource = new ClassPathResource(filePath).getFile();
        return  new String(Files.readAllBytes(resource.toPath()));
    }
    private void insertNewTree(String filePath) throws Exception {
        String payload = readTree(filePath);
        mvc.perform(put("/tree")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().is2xxSuccessful());
    }
}

